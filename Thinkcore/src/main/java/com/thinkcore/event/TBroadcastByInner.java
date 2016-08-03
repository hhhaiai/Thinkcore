/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkcore.event;

import java.util.ArrayList;
import java.util.LinkedList;

import com.thinkcore.TApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

/**
 * 广播执行者
 */
public class TBroadcastByInner {
	public static String TAG = TBroadcastByInner.class.getCanonicalName();
	public static final String INTENT_ACTION_EVENT = TAG
			+ ".INTENT_ACTION_EVENT" + "." + android.os.Process.myPid();
	public static final String MAINEVENT = "mainevent";
	public static final String EVENT = "event";
	public static final String MESSAGE = "message";
	public static final String MESSAGE0 = "message0";
	public static final String MESSAGE1 = "message1";
	public static final String MESSAGE2 = "message2";
	public static final String MESSAGE3 = "message3";
	public static final String MESSAGE4 = "message4";
	public static final String MESSAGE5 = "message5";

	private static LinkedList<IBroadcastByInner> sInnerListeners = new LinkedList<>();
	protected static ArrayList<String> mBroadcastParametersInner = new ArrayList<String>();

	private static BroadcastReceiver mBroadcastReceiver = null;

	static {
		initHandle();
	}

	private static void initHandle() {
		if (mBroadcastReceiver != null) {
			return;
		}

		mBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (sInnerListeners.isEmpty()
						|| !INTENT_ACTION_EVENT.equals(action)) {
					return;
				}

				initBroadcastParameterByInner(intent);// 初始化广播参数
				for (IBroadcastByInner listener : sInnerListeners) {
					if (listener.processBroadcastByInner(intent,
							mBroadcastParametersInner)) {
						return;
					}
				}
			}
		};
		registerBroadcast(TApplication.getInstance(), mBroadcastReceiver);
	}

	public static void sentEvent(int mainEvent, String... message) {
		sentEvent(mainEvent, 0, message);
	}

	public static void sentEvent(int mainEvent, int event, String... message) {

		Intent intent = new Intent(INTENT_ACTION_EVENT);
		intent.putExtra(MAINEVENT, mainEvent);
		intent.putExtra(EVENT, event);

		if (message != null) {
			for (int i = 0; i < message.length; i++) {
				intent.putExtra(String.format(MESSAGE + "%d", i), message[i]);
			}
		}
		TApplication.getInstance().sendBroadcast(intent);
	}

	public static void sentPostEvent(final int mainEvent, final int event,
			final int second, final String... message) {
		new AsyncTask<String, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {
				try {
					Thread.sleep(second * 1000);
				} catch (Exception e) {
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				sentEvent(mainEvent, event, message);
			}

		}.execute("");
	}

	public static void sentPostEvent(final int mainEvent, final int second,
			final String... message) {
		sentPostEvent(mainEvent, 0, second, message);
	}

	public static void registerBroadcast(Context context,
			BroadcastReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION_EVENT);
		filter.setPriority(1000);
		context.registerReceiver(receiver, filter);
	}

	public static void removeBroadcast(Context context,
			BroadcastReceiver receiver) {
		context.unregisterReceiver(receiver);
	}

	public static void addBroadcastListener(IBroadcastByInner listener) {
		sInnerListeners.add(listener);
	}

	public static void removeBroadcastListener(IBroadcastByInner listener) {
		sInnerListeners.remove(listener);
	}

	public static void removeBroadcastListener() {
		sInnerListeners.clear();
	}

	public interface IBroadcastByInner {
		boolean processBroadcastByInner(Intent intent, ArrayList<String> params);
	}

	private static void initBroadcastParameterByInner(Intent intent) {
		if (mBroadcastParametersInner == null)
			return;
		mBroadcastParametersInner.clear(); // Broadcast固定参数
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE0));
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE1));
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE2));
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE3));
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE4));
		mBroadcastParametersInner.add(intent
				.getStringExtra(TBroadcastByInner.MESSAGE5));
	}
}