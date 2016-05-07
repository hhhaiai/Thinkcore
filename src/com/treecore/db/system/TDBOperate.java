package com.treecore.db.system;

import com.treecore.TApplication;
import com.treecore.TIGlobalInterface;

import android.content.ContentResolver;
import android.content.Context;

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
}
