package com.thinkcore;

import java.util.List;

import com.morgoo.droidplugin.PluginApplication;
import com.thinkcore.utils.config.TPreferenceConfig;
import com.thinkcore.utils.config.TPropertiesConfig;
import com.thinkcore.utils.network.INetChangeListener;
import com.thinkcore.utils.network.TNetWorkUtil.netType;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;

//程序app
public class TApplication extends PluginApplication implements
		INetChangeListener {

	protected static TApplication mThis = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mThis = this;

		TPropertiesConfig.getInstance().initConfig(this);
		TPreferenceConfig.getInstance().initConfig(this);
	}

	@Override
	public void onTerminate() {
		onExitApplication();

		super.onTerminate();
	}

	@Override
	public void onConnect(netType type) {

	}

	@Override
	public void onDisConnect() {

	}

	public static TApplication getInstance() { // 获取程序实例
		return mThis;
	}

	protected void onExitApplication() { // 退出app
	}

	/**
	 * 退出应用程序
	 *
	 * @param isBackground
	 *            是否开开启后台运行
	 */
	public void appExit(Boolean isBackground) {
	}


	public PackageInfo getPackageInfo(int flags) {// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					flags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageInfo;
	}

	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}

	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 需要权限:android.permission.GET_TASKS
	 *
	 * @return
	 */
	public boolean isApplicationBroughtToBackground() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (tasks != null && !tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isProcess(String name) {
		String processName = getProcessName();
		if (processName.endsWith(name)) {
			return true;
		}

		return false;
	}

	public String getProcessName() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == android.os.Process.myPid()) {
				return procInfo.processName;
			}
		}
		return null;
	}
}
