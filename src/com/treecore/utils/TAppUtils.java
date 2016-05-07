package com.treecore.utils;

import java.io.File;
import java.util.List;

import com.treecore.TApplication;
import com.treecore.utils.log.TLog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.ClipboardManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TAppUtils {
	private static final String TAG = TAppUtils.class.getSimpleName();

	public static void clipToboard(String content) {
		ClipboardManager cmb = (ClipboardManager) TApplication.getInstance()
				.getApplicationContext()
				.getSystemService(Context.CLIPBOARD_SERVICE); // 得到剪贴板管理器
		cmb.setText(content);
	}

	/**
	 * @Description 判断是否是顶部activity
	 * @param context
	 * @param activityName
	 * @return
	 */
	public static boolean isTopActivy(Context context, String activityName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cName = am.getRunningTasks(1).size() > 0 ? am
				.getRunningTasks(1).get(0).topActivity : null;

		if (null == cName)
			return false;
		return cName.getClassName().equals(activityName);
	}

	/**
	 * 判断当前应用程序是否后台运行 在android5.0以上失效！请使用isApplicationBackground()方法代替！
	 * 
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Deprecated
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					TLog.d(TAG, "后台程序: " + appProcess.processName);
					return true;
				} else {
					TLog.d(TAG, "前台程序: " + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前应用程序处于前台还是后台 需要添加权限: <uses-permission
	 * android:name="android.permission.GET_TASKS" />
	 */
	public static boolean isApplicationBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				TLog.d(TAG, "isBackground: " + true);
				return true;
			}
		}
		TLog.d(TAG, "isBackground: " + false);
		return false;
	}

	/**
	 * 判断手机是否处理睡眠
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager kgMgr = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
		TLog.d(TAG, isSleeping ? "手机睡眠中.." : "手机未睡眠...");
		return isSleeping;
	}

	/**
	 * 安装apk
	 * 
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 初始化View的高宽
	 * 
	 * @param view
	 */
	@Deprecated
	public static void initViewWH(final View view) {
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						view.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						System.out.println(view + ", width: " + view.getWidth()
								+ "; height: " + view.getHeight());
					}
				});

	}

	/**
	 * 初始化View的高宽并显示不可见
	 * 
	 * @param view
	 */
	@Deprecated
	public static void initViewWHAndGone(final View view) {
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						view.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						view.setVisibility(View.GONE);
					}
				});

	}

	/**
	 * 获取当前应用程序的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context) {
		String version = "0";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			TLog.e(TAG, "getAppVersion", e);
		}
		TLog.d(TAG, "该应用的版本号: " + version);
		return version;
	}

	/**
	 * @Description 隐藏软键盘
	 * @param activity
	 */
	public static void hideInput(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void hideInput(EditText editText) {
		if (editText != null) {
			InputMethodManager inputmanger = (InputMethodManager) TApplication
					.getInstance().getSystemService(
							Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
