package com.thinkcore.activity;

import java.util.Stack;
import android.app.Activity;

//程序管理
public class TActivityManager {
	private static Stack<Activity> mActivityStack = new Stack<Activity>();// 进行Activity运行记录
	private static TActivityManager mThis;

	protected TActivity mCurrentActivity; // 当前activity

	private TActivityManager() {
	}

	/**
	 * 单一实例
	 */
	public static TActivityManager getInstance() {
		if (mThis == null) {
			mThis = new TActivityManager();
		}
		return mThis;
	}

	public int getSizeOfActivityStack() {
		return mActivityStack == null ? 0 : mActivityStack.size();
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		mActivityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = mActivityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 结束指定类名的Activity
	 */
	public boolean hasActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}
}