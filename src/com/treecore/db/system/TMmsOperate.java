package com.treecore.db.system;

import com.treecore.utils.TCursorUtils;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class TMmsOperate extends TDBOperate {
	public static Uri MMS_URI = Uri.parse("content://mms");
	public static final String FIELD_TABLE = "content://mms/";
	public static Uri SMS_MMS_URI = Uri
			.parse("content://mms-sms/conversations/");

	public Cursor getCursorBySMS_MMS() throws Exception { // 获取所有信息
		return getContentResolver().query(SMS_MMS_URI, null, null, null,
				"normalized_date desc");
	}

	public Cursor getCursorByAll() throws Exception { // 获取所有彩信
		return getContentResolver().query(MMS_URI, null, null, null,
				"date DESC");
	}

	public Cursor getCursorGroupByThreadId() throws Exception { // type1是接收到的，2是已发出
		return getContentResolver().query(MMS_URI, null,
				"0=0) GROUP BY (" + "thread_id", null, "date DESC");
	}

	public Cursor getCursorByThreadId(String threadId) throws Exception { // type1是接收到的，2是已发出
		if (TextUtils.isEmpty(threadId))
			throw new Exception("null by threadId");
		return getContentResolver().query(MMS_URI, null,
				"thread_id =" + threadId, null, "date ASC");
	}

	public void delMmsByAll() throws Exception {
		Cursor cursor = getCursorByAll();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = TCursorUtils.getInt(cursor, "_id");
				if (id != 0)
					delMmsById(id);
			} while (cursor.moveToNext());
		}

		if (cursor != null)
			cursor.close();
	}

	public int delMmsById(int id) throws Exception {
		if (id == 0)
			throw new Exception("null by id");

		int result = getContentResolver().delete(Uri.parse(FIELD_TABLE + id),
				null, null);
		return result;
	}

	public int getCountByThreadId(String threadId) { // type1是接收到的，2是已发出
		int result = 0;
		Cursor cursor = null;
		try {
			cursor = getCursorByThreadId(threadId);
		} catch (Exception e) {
		}
		if (cursor != null) {
			result = cursor.getCount();
			cursor.close();
		}
		return result;
	}
}
