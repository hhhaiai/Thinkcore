/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.thinkcore.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkcore.utils.log.TLog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 进程信息
 */
public class TProcessInfo {
	private static final String TAG = TProcessInfo.class.getSimpleName();

	/**
	 * get information of all running processes,including package name ,process
	 * name ,icon ,pid and uid.
	 * 
	 * @param context
	 *            context of activity
	 * @return running processes list
	 */
	public List<TPrograme> getRunningProcess(Context context) {
		TLog.i(TAG, "get running processes");

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
		PackageManager pm = context.getPackageManager();
		List<TPrograme> progressList = new ArrayList<TPrograme>();

		for (ApplicationInfo appinfo : getPackagesInfo(context)) {
			TPrograme programe = new TPrograme();
			if (((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
			// || ((appinfo.processName != null) && (appinfo.processName
			// .equals(PACKAGE_NAME)))
			) {
				continue;
			}
			for (RunningAppProcessInfo runningProcess : run) {
				if ((runningProcess.processName != null)
						&& runningProcess.processName
								.equals(appinfo.processName)) {
					programe.setPid(runningProcess.pid);
					programe.setUid(runningProcess.uid);
					break;
				}
			}

			// Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			// mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent,
			// PackageManager.MATCH_DEFAULT_ONLY);
			// for (ResolveInfo temp : appList) {
			// TLog.d("my logs", temp.activityInfo.packageName);
			// if (temp.activityInfo.packageName.toLowerCase().contains("uc")) {
			// TLog.v("my logs", "package and activity name = "
			// + temp.activityInfo.packageName + "    "
			// + temp.activityInfo.name);
			// }
			// }
			programe.setPackageName(appinfo.processName);
			programe.setProcessName(appinfo.loadLabel(pm).toString());
			programe.setIcon(appinfo.loadIcon(pm));
			progressList.add(programe);
		}
		Collections.sort(progressList);
		return progressList;
	}

	/**
	 * get information of all applications.
	 * 
	 * @param context
	 *            context of activity
	 * @return packages information of all applications
	 */
	private List<ApplicationInfo> getPackagesInfo(Context context) {
		PackageManager pm = context.getApplicationContext().getPackageManager();
		List<ApplicationInfo> appList = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		return appList;
	}
}
