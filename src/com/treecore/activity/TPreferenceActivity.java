package com.treecore.activity;

import com.treecore.TApplication;
import com.treecore.utils.TToastUtils;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public abstract class TPreferenceActivity extends PreferenceActivity {
	private String TAG = TPreferenceActivity.class.getCanonicalName();
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TActivityManager.getInstance().addActivity(this);// 添加activity
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		TActivityManager.getInstance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
	}

	protected void makeText(String content) {
		TToastUtils.makeText(mContext, content);
	}

	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}
}
