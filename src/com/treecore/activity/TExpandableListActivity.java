package com.treecore.activity;

import com.treecore.TApplication;

import android.app.ExpandableListActivity;
import android.content.res.Configuration;

public abstract class TExpandableListActivity extends ExpandableListActivity {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
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
		super.onDestroy();
	}
	
	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}
}
