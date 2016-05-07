/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.treecore.utils.network;

import java.util.ArrayList;

import com.treecore.TIGlobalInterface;
import com.treecore.utils.log.TLog;
import com.treecore.utils.network.TNetWorkUtil.netType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @Title 监听网络广播
 */
public class TNetworkStateReceiver extends BroadcastReceiver implements
		TIGlobalInterface {
	private static Boolean mNetworkAvailable = false;
	private static netType mNetType;
	private static ArrayList<TINetChangeListener> mNetChangeObserverArrayList = new ArrayList<TINetChangeListener>();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public final static String TA_ANDROID_NET_CHANGE_ACTION = "think.android.net.conn.CONNECTIVITY_CHANGE";
	private static TNetworkStateReceiver mThis;
	private Context mContext;

	public static TNetworkStateReceiver getInstance() {
		if (mThis == null) {
			mThis = new TNetworkStateReceiver();
		}
		return mThis;
	}

	public void initConfig(Context context) {
		mContext = context;
		registerNetworkStateReceiver();
	}

	@Override
	public void initConfig() {
	}

	@Override
	public void release() {
		unRegisterNetworkStateReceiver();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
				|| intent.getAction().equalsIgnoreCase(
						TA_ANDROID_NET_CHANGE_ACTION)) {
			TLog.i(TNetworkStateReceiver.this, "网络状态改变.");
			if (!TNetWorkUtil.isNetworkAvailable()) {
				TLog.i(TNetworkStateReceiver.this, "没有网络连接.");
				mNetworkAvailable = false;
			} else {
				TLog.i(TNetworkStateReceiver.this, "网络连接成功.");
				mNetType = TNetWorkUtil.getAPNType();
				mNetworkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * 注册网络状态广播
	 * 
	 * @param mContext
	 */
	private void registerNetworkStateReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(TA_ANDROID_NET_CHANGE_ACTION);
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext()
				.registerReceiver(getInstance(), filter);
	}

	/**
	 * 检查网络状态
	 * 
	 * @param mContext
	 */
	public void checkNetworkState() {
		Intent intent = new Intent();
		intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 注销网络状态广播
	 * 
	 * @param mContext
	 */
	private void unRegisterNetworkStateReceiver() {
		try {
			mContext.getApplicationContext().unregisterReceiver(this);
		} catch (Exception e) {
			TLog.d("TANetworkStateReceiver", e.getMessage());
		}
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public Boolean isNetworkAvailable() {
		return mNetworkAvailable;
	}

	public netType getAPNType() {
		return mNetType;
	}

	private void notifyObserver() {
		for (int i = 0; i < mNetChangeObserverArrayList.size(); i++) {
			TINetChangeListener observer = mNetChangeObserverArrayList.get(i);
			if (observer != null) {
				if (isNetworkAvailable()) {
					observer.onConnect(mNetType);
				} else {
					observer.onDisConnect();
				}
			}
		}

	}

	/**
	 * 注册网络连接观察者
	 * 
	 * @param observerKey
	 *            observerKey
	 */
	public static void registerObserver(TINetChangeListener observer) {
		if (mNetChangeObserverArrayList == null) {
			mNetChangeObserverArrayList = new ArrayList<TINetChangeListener>();
		}
		mNetChangeObserverArrayList.add(observer);
	}

	/**
	 * 注销网络连接观察者
	 * 
	 * @param resID
	 *            observerKey
	 */
	public void removeRegisterObserver(TINetChangeListener observer) {
		if (mNetChangeObserverArrayList != null) {
			mNetChangeObserverArrayList.remove(observer);
		}
	}

}