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

package com.treecore.http.core;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.treecore.utils.log.TLog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * Internal class, representing the HttpRequest, done in asynchronous manner
 */
public class AsyncHttpRequest implements Runnable {
	private static String TAG = AsyncHttpRequest.class.getSimpleName();

	private final AbstractHttpClient mClient;
	private final HttpContext mContext;
	private final HttpUriRequest mRequest;
	private final ResponseHandlerInterface mResponseHandler;
	private int mExecutionCount;
	private boolean mIsCancelled = false;
	private boolean mCancelIsNotified = false;
	private boolean mIsFinished = false;

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, ResponseHandlerInterface responseHandler) {
		this.mClient = client;
		this.mContext = context;
		this.mRequest = request;
		this.mResponseHandler = responseHandler;
	}

	@Override
	public void run() {
		if (isCancelled()) {
			return;
		}

		if (mResponseHandler != null) {
			mResponseHandler.sendStartMessage();
		}

		if (isCancelled()) {
			return;
		}

		try {
			makeRequestWithRetries();
		} catch (IOException e) {
			if (!isCancelled() && mResponseHandler != null) {
				mResponseHandler.sendFailureMessage(0, null, null, e);
			} else {
				TLog.e(TAG,
						"makeRequestWithRetries returned error, but handler is null"
								+ e.getMessage());
			}
		}

		if (isCancelled()) {
			return;
		}

		if (mResponseHandler != null) {
			mResponseHandler.sendFinishMessage();
		}

		mIsFinished = true;
	}

	private void makeRequest() throws IOException {
		if (isCancelled()) {
			return;
		}
		// Fixes #115
		if (mRequest.getURI().getScheme() == null) {
			// subclass of IOException so processed in the caller
			throw new MalformedURLException("No valid URI scheme was provided");
		}

		HttpResponse response = mClient.execute(mRequest, mContext);

		if (!isCancelled() && mResponseHandler != null) {
			mResponseHandler.sendResponseMessage(response);
		}
	}

	private void makeRequestWithRetries() throws IOException {
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = mClient
				.getHttpRequestRetryHandler();
		try {
			while (retry) {
				try {
					makeRequest();
					return;
				} catch (UnknownHostException e) {
					// switching between WI-FI and mobile data networks can
					// cause a retry which then results in an
					// UnknownHostException
					// while the WI-FI is initialising. The retry logic will be
					// invoked here, if this is NOT the first retry
					// (to assist in genuine cases of unknown host) which seems
					// better than outright failure
					cause = new IOException("UnknownHostException exception: "
							+ e.getMessage());
					retry = (mExecutionCount > 0)
							&& retryHandler.retryRequest(cause,
									++mExecutionCount, mContext);
				} catch (NullPointerException e) {
					// there's a bug in HttpClient 4.0.x that on some occasions
					// causes
					// DefaultRequestExecutor to throw an NPE, see
					// http://code.google.com/p/android/issues/detail?id=5255
					cause = new IOException("NPE in HttpClient: "
							+ e.getMessage());
					retry = retryHandler.retryRequest(cause, ++mExecutionCount,
							mContext);
				} catch (IOException e) {
					if (isCancelled()) {
						// Eating exception, as the request was cancelled
						return;
					}
					cause = e;
					retry = retryHandler.retryRequest(cause, ++mExecutionCount,
							mContext);
				}
				if (retry && (mResponseHandler != null)) {
					mResponseHandler.sendRetryMessage(mExecutionCount);
				}
			}
		} catch (Exception e) {
			// catch anything else to ensure failure message is propagated
			TLog.e(TAG, "Unhandled exception origin cause" + e.getMessage());
			cause = new IOException("Unhandled exception: " + e.getMessage());
		}

		// cleaned up to throw IOException
		throw (cause);
	}

	public boolean isCancelled() {
		if (mIsCancelled) {
			sendCancelNotification();
		}
		return mIsCancelled;
	}

	private synchronized void sendCancelNotification() {
		if (!mIsFinished && mIsCancelled && !mCancelIsNotified) {
			mCancelIsNotified = true;
			if (mResponseHandler != null)
				mResponseHandler.sendCancelMessage();
		}
	}

	public boolean isDone() {
		return isCancelled() || mIsFinished;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		mIsCancelled = true;
		mRequest.abort();
		return isCancelled();
	}
}
