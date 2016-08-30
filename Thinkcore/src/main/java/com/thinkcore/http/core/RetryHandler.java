/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

/*
 Some of the retry logic in this class is heavily borrowed from the
 fantastic droid-fu project: https://github.com/donnfelker/droid-fu
 */

package com.thinkcore.http.core;

import android.os.SystemClock;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.net.ssl.SSLException;

public class RetryHandler implements HttpRequestRetryHandler {
	private final static HashSet<Class<?>> mExceptionWhitelist = new HashSet();
	private final static HashSet<Class<?>> mExceptionBlacklist = new HashSet();

	static {
		// Retry if the server dropped connection on us
		mExceptionWhitelist.add(NoHttpResponseException.class);
		// retry-this, since it may happens as part of a Wi-Fi to 3G failover
		mExceptionWhitelist.add(UnknownHostException.class);
		// retry-this, since it may happens as part of a Wi-Fi to 3G failover
		mExceptionWhitelist.add(SocketException.class);

		// never retry timeouts
		mExceptionBlacklist.add(InterruptedIOException.class);
		// never retry SSL handshake failures
		mExceptionBlacklist.add(SSLException.class);
	}

	private final int maxRetries;
	private final int retrySleepTimeMS;

	public RetryHandler(int maxRetries, int retrySleepTimeMS) {
		this.maxRetries = maxRetries;
		this.retrySleepTimeMS = retrySleepTimeMS;
	}

	@Override
	public boolean retryRequest(IOException exception, int executionCount,
			HttpContext context) {
		boolean retry = true;

		Boolean b = (Boolean) context
				.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (b != null && b);

		if (executionCount > maxRetries) {
			// Do not retry if over max retry count
			retry = false;
		} else if (isInList(mExceptionWhitelist, exception)) {
			// immediately retry if error is whitelisted
			retry = true;
		} else if (isInList(mExceptionBlacklist, exception)) {
			// immediately cancel retry if the error is blacklisted
			retry = false;
		} else if (!sent) {
			// for most other errors, retry only if request hasn't been fully
			// sent yet
			retry = true;
		}

		if (retry) {
			// resend all idempotent requests
			HttpUriRequest currentReq = (HttpUriRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			if (currentReq == null) {
				return false;
			}
		}

		if (retry) {
			SystemClock.sleep(retrySleepTimeMS);
		} else {
			exception.printStackTrace();
		}

		return retry;
	}

	public static void addClassToWhitelist(Class<?> cls) {
		mExceptionWhitelist.add(cls);
	}

	public static void addClassToBlacklist(Class<?> cls) {
		mExceptionBlacklist.add(cls);
	}

	protected boolean isInList(HashSet<Class<?>> list, Throwable error) {
		for (Class<?> aList : list) {
			if (aList.isInstance(error)) {
				return true;
			}
		}
		return false;
	}
}
