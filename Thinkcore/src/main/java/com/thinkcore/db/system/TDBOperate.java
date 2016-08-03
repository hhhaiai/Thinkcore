package com.thinkcore.db.system;

import com.thinkcore.TApplication;
import com.thinkcore.TIGlobalInterface;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TDBOperate implements TIGlobalInterface {

	public Context getContext() {
		return TApplication.getInstance().getApplicationContext();
	}

	public ContentResolver getContentResolver() {
		return getContext().getContentResolver();
	}

	@Override
	public void initConfig() {
	}

	@Override
	public void release() {
	}

	public static boolean checkColumnExist(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
			// TLog.e("", "checkColumnExists1..." + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}
}
