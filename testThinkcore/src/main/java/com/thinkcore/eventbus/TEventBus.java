package com.thinkcore.eventbus;


import android.os.AsyncTask;

import de.greenrobot.event.EventBus;

public class TEventBus extends EventBus{
	private static final String TAG = TEventBus.class.getSimpleName();

	static volatile TEventBus defaultInstance;

	public static TEventBus getDefault() {
		if (defaultInstance == null) {
			synchronized (TEventBus.class) {
				if (defaultInstance == null) {
					defaultInstance = new TEventBus();
				}
			}
		}
		return defaultInstance;
	}

	/*
	* 扩展方法，增加时间延长
	* */
	public void post(final Object event, final int second) {
		new AsyncTask<String, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {
				try {
					Thread.sleep(second * 1000);
				} catch (Exception e) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				post(event);
			}
		}.execute("");
	}
}
