package com.thinkcore;

import java.util.HashMap;
import java.util.Iterator;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class TNotificationManager implements TIGlobalInterface {
	protected Context mContext;
	public NotificationManager mNotificationManager;
	protected HashMap<Integer, NotificationCompat.Builder> mBuilderMap = new HashMap<Integer, NotificationCompat.Builder>();

	public void initConfig(Context context) {
		mContext = context;
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public void initConfig() {
	}

	@Override
	public void release() {
		clearAllNotify();
	}

	public NotificationCompat.Builder createBuilder(int notifyId) {// 返回已创建否则再创建
		if (mBuilderMap.containsKey(notifyId))
			return mBuilderMap.get(notifyId);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				mContext);
		mBuilderMap.put(notifyId, builder);
		return builder;
	}

	public void showNotify(NotificationCompat.Builder builder) {
		if (!mBuilderMap.containsValue(builder))
			return;
		int notifyId = hasNotifyByBuilder(builder);
		if (notifyId == -1)
			return;

		mNotificationManager.notify(notifyId, builder.build());
	}

	public void clearNotify(int notifyId) {
		if (!mBuilderMap.containsKey(notifyId))
			return;
		mBuilderMap.remove(notifyId);
		mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
	}

	public void clearAllNotify() {
		mBuilderMap.clear();
		mNotificationManager.cancelAll();// 删除你发的所有通知
	}

	public NotificationCompat.Builder hasNotifyById(int notifyId) {
		if (!mBuilderMap.containsKey(notifyId))
			return null;

		return mBuilderMap.get(notifyId);
	}

	public int hasNotifyByBuilder(NotificationCompat.Builder builder) {
		Iterator iter = mBuilderMap.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Object val = mBuilderMap.get(key);
			if (val == builder)
				return (Integer) key;
		}

		return -1;
	}
}
