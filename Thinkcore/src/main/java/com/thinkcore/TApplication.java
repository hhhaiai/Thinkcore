package com.thinkcore;

import java.util.List;

import com.thinkcore.crash.TCrash;
import com.thinkcore.crash.TICrashListener;
import com.thinkcore.utils.config.TPreferenceConfig;
import com.thinkcore.utils.config.TPropertiesConfig;
import com.thinkcore.utils.log.TLog;
import com.thinkcore.utils.network.TINetChangeListener;
import com.thinkcore.utils.network.TNetWorkUtil.netType;
import com.thinkcore.utils.task.TITaskListener;
import com.thinkcore.utils.task.TTask;
import com.thinkcore.utils.task.TTask.Task;
import com.thinkcore.utils.task.TTask.TaskEvent;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

//程序app
public class TApplication extends Application implements TICrashListener,
		TITaskListener, TINetChangeListener {

	protected static TApplication mThis = null;
	private TTask mInitTask = null;
	private boolean mInit = false;

	@Override
	public void onCreate() {
		super.onCreate();
		mThis = this;

		TCrash.getInstance().setICrashListener(this);
		TCrash.getInstance().initConfig(this);

		TPropertiesConfig.getInstance().initConfig(this);
		TPreferenceConfig.getInstance().initConfig(this);

		mInitTask = new TTask();
		mInitTask.setIXTaskListener(this);
		mInitTask.startTask(100);
	}

	@Override
	public void onTerminate() {
		if (mInitTask != null)
			mInitTask.stopTask();

		mInitTask = null;
		onExitApplication();

		super.onTerminate();
	}

	@Override
	public void onAppCrash(String crashFile) {

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
		TCrash.getInstance().release();

		TPropertiesConfig.getInstance().release();
		TPreferenceConfig.getInstance().release();
	}

	protected void onInitConfigByThread() {
	}

	public void onInitComplete() {

	}

	public boolean isInitComplete() {
		return mInit;
	}

	/**
	 * 退出应用程序
	 *
	 * @param isBackground
	 *            是否开开启后台运行
	 */
	public void appExit(Boolean isBackground) {
	}

	@Override
	public void onTask(Task task, TaskEvent event, Object... params) {
		if (mInitTask != null && mInitTask.equalTask(task)) {
			try {
				if (event == TaskEvent.Work) {
					onInitConfigByThread();
				} else if (event == TaskEvent.Cancel) {
					mInit = true;
					onInitComplete();
				}
			} catch (Exception e) {
			}
		}
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

	public static boolean isApkDebugable() {
		try {
			ApplicationInfo info= TApplication.getInstance().getApplicationInfo();
			return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
		} catch (Exception e) {

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
