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

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * A wrapper class around {@link Cookie} and/or {@link BasicClientCookie}
 * designed for use in {@link PersistentCookieStore}.
 */
public class SerializableCookie implements Serializable {
	private static final long serialVersionUID = 6374381828722046732L;

	private transient final Cookie mCookie;
	private transient BasicClientCookie mClientCookie;

	public SerializableCookie(Cookie cookie) {
		this.mCookie = cookie;
	}

	public Cookie getCookie() {
		Cookie bestCookie = mCookie;
		if (mClientCookie != null) {
			bestCookie = mClientCookie;
		}
		return bestCookie;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(mCookie.getName());
		out.writeObject(mCookie.getValue());
		out.writeObject(mCookie.getComment());
		out.writeObject(mCookie.getDomain());
		out.writeObject(mCookie.getExpiryDate());
		out.writeObject(mCookie.getPath());
		out.writeInt(mCookie.getVersion());
		out.writeBoolean(mCookie.isSecure());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		String name = (String) in.readObject();
		String value = (String) in.readObject();
		mClientCookie = new BasicClientCookie(name, value);
		mClientCookie.setComment((String) in.readObject());
		mClientCookie.setDomain((String) in.readObject());
		mClientCookie.setExpiryDate((Date) in.readObject());
		mClientCookie.setPath((String) in.readObject());
		mClientCookie.setVersion(in.readInt());
		mClientCookie.setSecure(in.readBoolean());
	}
}