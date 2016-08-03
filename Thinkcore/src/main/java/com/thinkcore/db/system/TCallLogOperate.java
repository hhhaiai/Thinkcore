package com.thinkcore.db.system;

import com.thinkcore.utils.TCursorUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.text.TextUtils;

public class TCallLogOperate extends TDBOperate {
	public static Uri CALLLOG_URI = CallLog.Calls.CONTENT_URI;

	public Cursor getCursorByAll() throws Exception { // 获取通话记录
		return getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
				null, null, "date DESC");
		// /data/data/com.android.providers.contacts/databases/contacts2.db
		// 版本不同 位置有点不同
		// /data/tata/com.andriod.providers.contacts/databases/contacts.db
	}

	public long getLastDateByNumber(String number) { // 获得指定号码的记录
		long date = 0;
		if (TextUtils.isEmpty(number))
			return date;
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
					null, "number = '" + number + "'", null, "date DESC");
			if (cursor != null && cursor.moveToFirst()) {
				date = Long.parseLong(TCursorUtils.getString(cursor,
						CallLog.Calls.DATE)); // 日期
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null) {
			cursor.close();
		}

		return date;
	}

	public boolean hasCallRecord(String number, long date) { //
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = getCursorByNumberAndDate(number, date);
			if (cursor != null && cursor.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
		}
		if (cursor != null)
			cursor.close();
		return result;
	}

	public Cursor getCursorByNumberAndDate(String number, long date)
			throws Exception {
		if (TextUtils.isEmpty(number))
			throw new Exception("null by number...");
		if (date == 0)
			throw new Exception("null by date...");

		return getContentResolver().query(
				CallLog.Calls.CONTENT_URI,
				null,
				CallLog.Calls.NUMBER + " = '" + number + "' and "
						+ CallLog.Calls.DATE + " = " + date, null, "date DESC");
	}

	public Cursor getCursorByNumber(String number) throws Exception { // 获得指定号码的记录
		if (TextUtils.isEmpty(number))
			throw new Exception("null by number...");

		return getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
				"number = '" + number + "'", null, "date DESC");
	}

	public Cursor getCursorById(int id) throws Exception { // 获得指定id的记录
		if (id == 0)
			throw new Exception("null by id");
		return getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
				CallLog.Calls._ID + "=" + id, null, null);
	}

	public int delByNumber(String number) throws Exception { // 删除指定通话记录
		if (TextUtils.isEmpty(number))
			throw new Exception("null by number...");
		return getContentResolver().delete(CallLog.Calls.CONTENT_URI,
				CallLog.Calls.NUMBER + "=?", new String[] { number });
	}

	public int delById(int id) throws Exception { // 删除指定通话记录
		if (id == 0)
			throw new Exception("null by id");
		return getContentResolver().delete(CallLog.Calls.CONTENT_URI,
				CallLog.Calls._ID + "=?", new String[] { String.valueOf(id) });
	}

	public int delById(String id) throws Exception { // 删除指定通话记录
		if (TextUtils.isEmpty(id))
			throw new Exception("null by id...");
		return getContentResolver().delete(CallLog.Calls.CONTENT_URI,
				CallLog.Calls._ID + "=?", new String[] { id });
	}

	public void delAll() throws Exception { // 删除所有通话记录
		getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
	}

	// callType 1 来电 2去电 3丢失
	public void addEotuCallLog(int callType, String number, String name,
			long time, long duration, int read) throws Exception { // 添加通话记录（优途）
		ContentValues content = new ContentValues();
		content.put(CallLog.Calls.TYPE, callType);
		content.put(CallLog.Calls.NUMBER, number);
		content.put(CallLog.Calls.DATE, time);
		content.put(CallLog.Calls.CACHED_NAME, name);
		content.put(CallLog.Calls.NEW, read);// 0已看1未看
		content.put(CallLog.Calls.DURATION, duration);// 通话时长
		// content.put("subscription", CallRecord.SUBSCRIPTION_EOTU);// eotu拨打
		// content.put(CallLog.Calls.CACHED_NUMBER_TYPE, 3);// eotu拨打
		getContentResolver().insert(CallLog.Calls.CONTENT_URI, content);
	}

	public void modifyCallLog(int type, String number, long time, int read)
			throws Exception { // 修改通话记录
		ContentValues content = new ContentValues();
		content.put(CallLog.Calls.TYPE, type);
		content.put(CallLog.Calls.NUMBER, number);
		content.put(CallLog.Calls.DATE, time);
		content.put(CallLog.Calls.NEW, read);// 0已看1未看
		getContentResolver().update(CallLog.Calls.CONTENT_URI, content,
				CallLog.Calls.NUMBER + "=?", new String[] { number });
	}
}
