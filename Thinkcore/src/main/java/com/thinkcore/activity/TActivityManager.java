package com.thinkcore.activity;

import java.util.Stack;

import com.thinkcore.activity.layoutloader.TILayoutLoader;
import com.thinkcore.activity.layoutloader.TLayoutLoader;

import android.app.Activity;

//程序管理
public class TActivityManager {
	private static Stack<Activity> mActivityStack = new Stack<Activity>();// 进行Activity运行记录
	private static TActivityManager mThis;

	protected TActivity mCurrentActivity; // 当前activity

	/** 获取布局文件ID加载器 */
	protected TILayoutLoader mLayoutLoader;
	/** 加载类注入器 */
	protected TInjector mInjector;

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

	// 返回
	public void back() {
		// if (mActivityStackInfo != null && mActivityStackInfo.size() != 0) {
		// if (mActivityStackInfo.size() >= 2) {
		// mActivityStackInfo.pop();
		// }
		//
		// mCurrentNavigationDirection = NavigationDirection.Backward;
		// ActivityStackInfo info = mActivityStackInfo.peek();
		// try {
		// TCommandExecutor.getInstance().enqueueCommand(
		// info.getCommandKey(), info.getRequest(), this);
		// } catch (TNoSuchCommandException e) {
		// e.printStackTrace();
		// }
		// }
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

	public TILayoutLoader getLayoutLoader() { // 布局加载
		if (mLayoutLoader == null) {
			mLayoutLoader = TLayoutLoader.getInstance();
		}
		return mLayoutLoader;
	}

	public void setLayoutLoader(TILayoutLoader layoutLoader) { // 设置布局加载
		mLayoutLoader = layoutLoader;
	}

	public TInjector getInjector() { // 注入
		if (mInjector == null) {
			mInjector = TInjector.getInstance();
		}
		return mInjector;
	}

	public void setInjector(TInjector injector) { // 设置注入
		this.mInjector = injector;
	}
}