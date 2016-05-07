package com.treecore.activity;

import java.util.ArrayList;

import com.treecore.TApplication;
import com.treecore.dialog.TDialogManager;
import com.treecore.utils.TActivityUtils;
import com.treecore.utils.THandler;
import com.treecore.utils.TStringUtils;
import com.treecore.utils.TToastUtils;
import com.treecore.utils.task.TITaskListener;
import com.treecore.utils.task.TTask;
import com.treecore.utils.task.TTask.Task;
import com.treecore.utils.task.TTask.TaskEvent;

import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

//界面
public abstract class TTabActivity extends TabActivity implements
		TITaskListener {
	private String TAG = TActivity.class.getCanonicalName();

	public enum Status {
		NONE, CREATED, STARTED, RESUMED, PAUSED, STOPPED, DESTORYED
	}

	/** 模块的名字 */
	private String mModuleName = "";
	/** 布局文件的名字 */
	private String mLayouName = "";

	protected Context mContext;
	protected TTask mActivityTask;
	private Status mStatus;
	protected ArrayList<String> mActivityParameters = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mStatus = Status.CREATED;

		initActivityParameter(getIntent());

		TActivityManager.getInstance().addActivity(this);// 添加activity
		getModuleName();// 初始化模块名
		if (TStringUtils.isEmpty(mLayouName))
			mLayouName = mContext.getPackageName();
		initInjector();// 加载类注入器
		loadDefautLayout();// 自动加载默认布局
	}

	@Override
	public void setContentView(int layoutResID) { // 设置视图
		super.setContentView(layoutResID);
		TActivityManager.getInstance().getInjector().injectView(this); // 由于view必须在视图记载之后添加注入
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onStart() {
		mStatus = Status.STARTED;
		super.onStart();
	}

	@Override
	protected void onResume() {
		mStatus = Status.RESUMED;
		super.onResume();
	}

	@Override
	protected void onPause() {
		mStatus = Status.PAUSED;
		super.onPause();
	}

	@Override
	protected void onStop() {
		mStatus = Status.STOPPED;
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		TActivityManager.getInstance().removeActivity(this);

		TDialogManager.hideProgressDialog(this);

		if (mActivityParameters != null)
			mActivityParameters.clear();
		mActivityParameters = null;

		stopTask();
		mActivityTask = null;

		mStatus = Status.DESTORYED;

		mContext = null;
		super.onDestroy();
	}

	@Override
	public void onTask(Task task, TaskEvent event, Object... params) {
	}

	protected void startTask(int taskID, String... params) {
		if (mActivityTask == null) {
			mActivityTask = new TTask();
			mActivityTask.setIXTaskListener(this);
		}

		mActivityTask.startTask(taskID, params);
	}

	protected void stopTask() {
		if (mActivityTask != null)
			mActivityTask.stopTask();
	}

	protected void handleMessage(Message msg) {
	}

	/**
	 * 初始化注入器
	 */
	private void initInjector() {
		TActivityManager.getInstance().getInjector().injectResource(this);
		TActivityManager.getInstance().getInjector().inject(this);
	}

	/**
	 * 自动加载默认布局
	 */
	private void loadDefautLayout() {
		try {
			int layoutResID = TActivityManager.getInstance().getLayoutLoader()
					.getLayoutID(mLayouName);
			setContentView(layoutResID);
		} catch (Exception e) {
		}
	}

	public void setContentView(View view, LayoutParams params) {// 设置视图
		super.setContentView(view, params);
		// 由于view必须在视图记载之后添加注入
		TActivityManager.getInstance().getInjector().injectView(this);
	}

	public void setContentView(View view) {// 设置视图
		super.setContentView(view);
		// 由于view必须在视图记载之后添加注入
		TActivityManager.getInstance().getInjector().injectView(this);
	}

	public String getModuleName() {// 获取模块的名字
		if (mModuleName == null || mModuleName.equalsIgnoreCase("")) {
			mModuleName = getClass().getName().substring(0,
					getClass().getName().length() - 8);
			String arrays[] = mModuleName.split("\\.");
			mModuleName = mModuleName = arrays[arrays.length - 1].toLowerCase();
		}
		return mModuleName;
	}

	public void setModuleName(String moduleName) {// 设置模块的名字
		mModuleName = moduleName;
	}

	private void initActivityParameter(Intent intent) {
		if (mActivityParameters == null)
			return;
		mActivityParameters.clear(); // Activity固定参数
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA0));
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA1));
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA2));
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA3));
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA4));
		mActivityParameters.add(intent
				.getStringExtra(TActivityUtils.FIELD_DATA5));
	}

	protected ArrayList<String> getActivityParameter() {
		return mActivityParameters;
	}

	public Status get_status() {
		return mStatus;
	}

	public boolean isActivity() {
		return mStatus != Status.DESTORYED && mStatus != Status.PAUSED
				&& mStatus != Status.STOPPED;
	}

	protected void makeText(String content) {
		TToastUtils.makeText(mContext, content);
	}

	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}
}
