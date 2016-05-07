package com.sqlcrypt.database;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

//除了注册监听 其它都是对等转换
public class TCursor implements android.database.Cursor {
	private Cursor mCryptCursor;

	public TCursor(Cursor cursor) {
		mCryptCursor = cursor;
	}

	@Override
	public void close() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;

		mCryptCursor.close();
	}

	@Override
	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
		com.sqlcrypt.database.CharArrayBuffer buffer2 = new com.sqlcrypt.database.CharArrayBuffer(
				buffer.data);
		mCryptCursor.copyStringToBuffer(columnIndex, buffer2);
	}

	@Override
	public void deactivate() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
		mCryptCursor.deactivate();
	}

	@Override
	public byte[] getBlob(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return mCryptCursor.getBlob(columnIndex);
	}

	@Override
	public int getColumnCount() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getColumnCount();
	}

	@Override
	public int getColumnIndex(String columnName) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getColumnIndex(columnName);
	}

	@Override
	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getColumnIndexOrThrow(columnName);
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return mCryptCursor.getColumnName(columnIndex);
	}

	@Override
	public String[] getColumnNames() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return mCryptCursor.getColumnNames();
	}

	@Override
	public int getCount() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getCount();
	}

	@Override
	public double getDouble(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getDouble(columnIndex);
	}

	@Override
	public Bundle getExtras() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return mCryptCursor.getExtras();
	}

	@Override
	public float getFloat(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getFloat(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getLong(columnIndex);
	}

	@Override
	public int getPosition() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getPosition();
	}

	@Override
	public short getShort(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getShort(columnIndex);
	}

	@Override
	public String getString(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return mCryptCursor.getString(columnIndex);
	}

	@Override
	public boolean getWantsAllOnMoveCalls() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.getWantsAllOnMoveCalls();
	}

	@Override
	public boolean isAfterLast() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.isBeforeFirst();
	}

	@Override
	public boolean isClosed() {
		if (mCryptCursor == null)
			return false;
		return mCryptCursor.isClosed();
	}

	@Override
	public boolean isFirst() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.isFirst();
	}

	@Override
	public boolean isLast() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.isLast();
	}

	@Override
	public boolean isNull(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.isNull(columnIndex);
	}

	@Override
	public boolean move(int offset) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.move(offset);
	}

	@Override
	public boolean moveToFirst() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.moveToFirst();
	}

	@Override
	public boolean moveToLast() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.moveToLast();
	}

	@Override
	public boolean moveToNext() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.moveToNext();
	}

	@Override
	public boolean moveToPosition(int position) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.moveToPosition(position);
	}

	@Override
	public boolean moveToPrevious() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return mCryptCursor.moveToPrevious();
	}

	@Override
	public void registerContentObserver(ContentObserver observer) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
		// mCryptCursor.registerContentObserver(observer);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
	}

	@Override
	public boolean requery() {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return false;
		return false;
	}

	@Override
	public Bundle respond(Bundle extras) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return null;
		return null;
	}

	@Override
	public void setNotificationUri(ContentResolver cr, Uri uri) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
		mCryptCursor.setNotificationUri(cr, uri);
	}

	@Override
	public void unregisterContentObserver(ContentObserver observer) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return;
	}

	@Override
	public int getType(int columnIndex) {
		if (mCryptCursor == null || mCryptCursor.isClosed())
			return 0;
		return mCryptCursor.getType(columnIndex);
	}

	@Override
	public Uri getNotificationUri() {
		return null;
	}
}
