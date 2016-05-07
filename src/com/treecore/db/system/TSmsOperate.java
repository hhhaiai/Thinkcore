package com.treecore.db.system;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.treecore.utils.TCursorUtils;
import com.treecore.utils.TStringUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class TSmsOperate extends TDBOperate {
	public static final Uri SMS_URL = Uri.parse("content://sms/");
	public static final String SMS_URL_OPERATOR = "content://sms/conversations/";

	public static final String INTENT_ACTION_SENT_SMS = TSmsOperate.class
			.getCanonicalName() + ".INTENT_ACTION_SENT_SMS";

	private static final String[] ID_PROJECTION = { "_id" };
	private static final String STANDARD_ENCODING = "UTF-8";
	private static final Uri THREAD_ID_CONTENT_URI = Uri
			.parse("content://mms-sms/threadID");
	public static final Uri CONTENT_URI = Uri.withAppendedPath(
			Uri.parse("content://mms-sms/"), "conversations");
	public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(
			CONTENT_URI, "obsolete");
	public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern
			.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");

	public boolean hasUnreadByLastId(long ThreadId) {
		boolean result = false;
		if (ThreadId == 0)
			return result;

		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(SMS_URL, null,
					" thread_id = " + ThreadId, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null && cursor.moveToFirst()) {
			result = TCursorUtils.getInt(cursor, "read") == 0 ? true : false;
			cursor.close();
		}
		return result;
	}

	public boolean hasUnreadByThreadId(long threadId) {
		boolean result = false;
		if (threadId == 0)
			return result;

		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(SMS_URL, null,
					" read = 0 ) GROUP BY (" + "thread_id", null, "date DESC");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null) {
			result = cursor.getCount() > 0;
			cursor.close();
		}
		return result;
	}

	public void sendSysMessage(String number, String person, String message)
			throws Exception { // 添加系统无需手动添加
		Intent sentIntent = new Intent(INTENT_ACTION_SENT_SMS);
		// PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0,
		// sentIntent, 0);
		android.telephony.SmsManager.getDefault().sendTextMessage(number, null,
				message, null, null);
		ContentValues contentValues = createNewDefaultMessage(number, person,
				message);
		addMessage(contentValues);
		contentValues = null;
	}

	public ContentValues createNewDefaultMessage(String number, String person,
			String message) {
		return createNewMessage(number, person, message, 0, 2, 1, "");
	}

	public ContentValues createNewMessage(String number, String person,
			String message, int status, int type, int read, String serviceCenter) {
		ContentValues cv = new ContentValues();
		cv.put("thread_id", getOrCreateThreadId(number));
		cv.put("address", number);
		cv.put("date", System.currentTimeMillis());
		cv.put("protocol", 0);
		cv.put("person", person);
		cv.put("read", read);
		cv.put("status", status);
		cv.put("type", type);
		cv.put("body", message);
		if (!TStringUtils.isEmpty(serviceCenter))
			cv.put("service_center", serviceCenter);
		cv.put("seen", 0);
		return cv;
	}

	public Uri addMessage(ContentValues message) throws Exception {
		if (message == null)
			throw new Exception("null by message");
		return getContentResolver().insert(SMS_URL, message);
	}

	public String getNumberByThreadID(long threadID) throws Exception {
		if (threadID == 0)
			throw new Exception("null by threadID");

		String result = "";
		Cursor cursor = getContentResolver().query(SMS_URL, null,
				"thread_id = " + threadID, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				result = TCursorUtils.getString(cursor, "address");
				if (!result.equals("")) {
					break;
				}
			} while (cursor.moveToNext());
		}

		if (cursor != null)
			cursor.close();

		return result;
	}

	public Cursor getCursorByAll() throws Exception { // type 1是接收到的，2是已发出
		return getContentResolver().query(SMS_URL, null, null, null,
				"date DESC");
	}

	public Cursor getCursorGroupByThreadId() throws Exception { // type1是接收到的，2是已发出
		return getContentResolver().query(SMS_URL, null,
				"0=0 ) GROUP BY (" + "thread_id", null, "date DESC");
	}

	public Cursor getCursorByThreadId(String threadId) throws Exception { // type1是接收到的，2是已发出
		if (TextUtils.isEmpty(threadId))
			throw new Exception("null by threadId");
		return getContentResolver().query(SMS_URL, null,
				"thread_id = " + threadId, null, "date DESC");
	}

	public int setReadedByThreadId(String threadId) throws Exception {
		if (TextUtils.isEmpty(threadId))
			throw new Exception("null by threadId");
		ContentValues contentValues = new ContentValues(1);
		contentValues.put("read", 1);
		return getContentResolver().update(SMS_URL, contentValues,
				"thread_id =?", new String[] { threadId });
	}

	public Cursor getMessageByNumberAndDate(String number, long date)
			throws Exception {
		if (TextUtils.isEmpty(number))
			throw new Exception("null by number");
		if (date == 0)
			throw new Exception("null by date");
		return getContentResolver().query(SMS_URL, null,
				"address = '" + number + "' and " + "date = " + date, null,
				null);
	}

	public long delMessageById(long _id, int id) throws Exception { // 根据id删除某条
		if (_id == 0 || id == 0)
			return 0;
		return getContentResolver().delete(Uri.parse(SMS_URL_OPERATOR + _id),
				"_id = " + id, null);
	}

	public long delMessageByThreadId(long thread_id) throws Exception { // 根据treahd_id删除某组
		if (thread_id == 0)
			return 0;
		return getContentResolver().delete(
				Uri.parse(SMS_URL_OPERATOR + thread_id), null, null);
	}

	public void delAllMessage() { // 删除全部
		Cursor cursor = null;

		try {
			cursor = getCursorGroupByThreadId();

			if (cursor != null && cursor.moveToFirst()) {
				do {
					long threadId = cursor.getLong(1);
					delMessageByThreadId(threadId);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null)
			cursor.close();
	}

	public boolean hasThreadId(String threadId) {
		boolean result = false;
		Cursor cursor = null;

		try {
			cursor = getCursorByThreadId(threadId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null) {
			result = cursor.getCount() > 0;
			cursor.close();
		}

		return result;
	}

	public int getCountByThreadId(String threadId) { // type1是接收到的，2是已发出
		int result = 0;

		Cursor cursor = null;
		try {
			cursor = getCursorByThreadId(threadId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null) {
			result = cursor.getCount();
			cursor.close();
		}

		return result > 0 ? result : 0;
	}

	public long getLastDateByNumber(String number) { // 获得指定号码的记录
		long date = 0;
		if (TextUtils.isEmpty(number))
			return date;

		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(SMS_URL, null,
					"address = '" + number + "') GROUP BY (address", null,
					"date DESC");

			if (cursor != null && cursor.moveToFirst()) {
				date = Long.parseLong(TCursorUtils.getString(cursor, "date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null) {
			cursor.close();
		}
		return date;
	}

	/**
	 * This is a single-recipient version of getOrCreateThreadId. It's
	 * convenient for use with SMS messages.
	 */
	public long getOrCreateThreadId(String address) {
		Set<String> recipients = new HashSet<String>();

		recipients.add(address);
		return getOrCreateThreadId(recipients);
	}

	/**
	 * Given the recipients list and subject of an unsaved message, return its
	 * thread ID. If the message starts a new thread, allocate a new thread ID.
	 * Otherwise, use the appropriate existing thread ID.
	 * 
	 * Find the thread ID of the same set of recipients (in any order, without
	 * any additions). If one is found, return it. Otherwise, return a unique
	 * thread ID.
	 */
	public long getOrCreateThreadId(Set<String> recipients) {
		Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();

		for (String recipient : recipients) {
			if (TStringUtils.isEmail(recipient)) {
				recipient = extractAddrSpec(recipient);
			}

			uriBuilder.appendQueryParameter("recipient", recipient);
		}

		Uri uri = uriBuilder.build();
		// if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);

		Cursor cursor = getContentResolver().query(uri, ID_PROJECTION, null,
				null, null);
		if (true) {
			// Log.v("Threads",
			// "getOrCreateThreadId cursor cnt: " + cursor.getCount());
		}
		long result = 0;
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					result = cursor.getLong(0);
					cursor.close();
					return result;
				} else {
					Log.e("Threads", "getOrCreateThreadId returned no rows!");
				}
			} catch (Exception e) {
			}

			cursor.close();
		}

		Log.e("Threads",
				"getOrCreateThreadId failed with uri " + uri.toString());
		throw new IllegalArgumentException(
				"Unable to find or allocate a thread ID.");
	}

	private static String extractAddrSpec(String address) {
		Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);

		if (match.matches()) {
			return match.group(2);
		}
		return address;
	}
}
