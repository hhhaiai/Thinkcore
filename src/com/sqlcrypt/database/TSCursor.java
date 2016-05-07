package com.sqlcrypt.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

//除了注册监听 其它都是对等转换
public class TSCursor implements Cursor {
	private android.database.Cursor mSCursor;

	public TSCursor(android.database.Cursor cursor) {
		mSCursor = cursor;
	}

	@Override
	public void close() {
		if (mSCursor == null || mSCursor.isClosed())
			return;

		mSCursor.close();
	}

	@Override
	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
		android.database.CharArrayBuffer buffer2 = new android.database.CharArrayBuffer(
				buffer.data);
		mSCursor.copyStringToBuffer(columnIndex, buffer2);
	}

	@Override
	public void deactivate() {
		if (mSCursor == null || mSCursor.isClosed())
			return;
		mSCursor.deactivate();
	}

	@Override
	public byte[] getBlob(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return mSCursor.getBlob(columnIndex);
	}

	@Override
	public int getColumnCount() {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getColumnCount();
	}

	@Override
	public int getColumnIndex(String columnName) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getColumnIndex(columnName);
	}

	@Override
	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getColumnIndexOrThrow(columnName);
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return mSCursor.getColumnName(columnIndex);
	}

	@Override
	public String[] getColumnNames() {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return mSCursor.getColumnNames();
	}

	@Override
	public int getCount() {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getCount();
	}

	@Override
	public double getDouble(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getDouble(columnIndex);
	}

	@Override
	public Bundle getExtras() {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return mSCursor.getExtras();
	}

	@Override
	public float getFloat(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getFloat(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getLong(columnIndex);
	}

	@Override
	public int getPosition() {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getPosition();
	}

	@Override
	public short getShort(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getShort(columnIndex);
	}

	@Override
	public String getString(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return mSCursor.getString(columnIndex);
	}

	@Override
	public boolean getWantsAllOnMoveCalls() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.getWantsAllOnMoveCalls();
	}

	@Override
	public boolean isAfterLast() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.isBeforeFirst();
	}

	@Override
	public boolean isClosed() {
		if (mSCursor == null)
			return false;
		return mSCursor.isClosed();
	}

	@Override
	public boolean isFirst() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.isFirst();
	}

	@Override
	public boolean isLast() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.isLast();
	}

	@Override
	public boolean isNull(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.isNull(columnIndex);
	}

	@Override
	public boolean move(int offset) {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.move(offset);
	}

	@Override
	public boolean moveToFirst() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.moveToFirst();
	}

	@Override
	public boolean moveToLast() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.moveToLast();
	}

	@Override
	public boolean moveToNext() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.moveToNext();
	}

	@Override
	public boolean moveToPosition(int position) {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.moveToPosition(position);
	}

	@Override
	public boolean moveToPrevious() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return mSCursor.moveToPrevious();
	}

	@Override
	public void registerContentObserver(ContentObserver observer) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
		// mSCursor.registerContentObserver(observer);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
	}

	@Override
	public boolean requery() {
		if (mSCursor == null || mSCursor.isClosed())
			return false;
		return false;
	}

	@Override
	public Bundle respond(Bundle extras) {
		if (mSCursor == null || mSCursor.isClosed())
			return null;
		return null;
	}

	@Override
	public void setNotificationUri(ContentResolver cr, Uri uri) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
		mSCursor.setNotificationUri(cr, uri);
	}

	@Override
	public void unregisterContentObserver(ContentObserver observer) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (mSCursor == null || mSCursor.isClosed())
			return;
	}

	@Override
	public int getType(int columnIndex) {
		if (mSCursor == null || mSCursor.isClosed())
			return 0;
		return mSCursor.getType(columnIndex);
	}
}
