/*
 *  Copyright 2012 Kevin Gaudin
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
 */

package com.thinkcore.crash.collector;

import static com.thinkcore.crash.TCrash.TAG;
import static com.thinkcore.crash.data.ReportField.ANDROID_VERSION;
import static com.thinkcore.crash.data.ReportField.APP_VERSION_CODE;
import static com.thinkcore.crash.data.ReportField.APP_VERSION_NAME;
import static com.thinkcore.crash.data.ReportField.AVAILABLE_MEM_SIZE;
import static com.thinkcore.crash.data.ReportField.BRAND;
import static com.thinkcore.crash.data.ReportField.BUILD;
import static com.thinkcore.crash.data.ReportField.CRASH_CONFIGURATION;
import static com.thinkcore.crash.data.ReportField.CUSTOM_DATA;
import static com.thinkcore.crash.data.ReportField.DEVICE_FEATURES;
import static com.thinkcore.crash.data.ReportField.DEVICE_ID;
import static com.thinkcore.crash.data.ReportField.DISPLAY;
import static com.thinkcore.crash.data.ReportField.DUMPSYS_MEMINFO;
import static com.thinkcore.crash.data.ReportField.ENVIRONMENT;
import static com.thinkcore.crash.data.ReportField.FILE_PATH;
import static com.thinkcore.crash.data.ReportField.INITIAL_CONFIGURATION;
import static com.thinkcore.crash.data.ReportField.INSTALLATION_ID;
import static com.thinkcore.crash.data.ReportField.PACKAGE_NAME;
import static com.thinkcore.crash.data.ReportField.PHONE_MODEL;
import static com.thinkcore.crash.data.ReportField.PRODUCT;
import static com.thinkcore.crash.data.ReportField.SETTINGS_SECURE;
import static com.thinkcore.crash.data.ReportField.SETTINGS_SYSTEM;
import static com.thinkcore.crash.data.ReportField.SHARED_PREFERENCES;
import static com.thinkcore.crash.data.ReportField.STACK_TRACE;
import static com.thinkcore.crash.data.ReportField.THREAD_DETAILS;
import static com.thinkcore.crash.data.ReportField.TOTAL_MEM_SIZE;
import static com.thinkcore.crash.data.ReportField.USER_CRASH_DATE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thinkcore.TApplication;
import com.thinkcore.crash.TCrash;
import com.thinkcore.crash.data.CrashReportData;
import com.thinkcore.crash.data.ReportField;
import com.thinkcore.storage.TFilePath;
import com.thinkcore.storage.TStorageUtils;
import com.thinkcore.utils.TAndroidVersionUtils;
import com.thinkcore.utils.TFileInstallationUtils;
import com.thinkcore.utils.TPackageUtils;
import com.thinkcore.utils.TScreenUtils;
import com.thinkcore.utils.log.TLog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.text.format.Time;

/**
 * 异常数据报告生成
 */
public class CrashReportDataFactory {// modify
	private final Context context;
	private final Map<String, String> customParameters = new HashMap<String, String>();
	private final Time appStartDate = new Time();
	private final String initialConfiguration;

	public CrashReportDataFactory(Context context) {// 初始化配置信息
		this.context = context;
		appStartDate.setToNow();
		initialConfiguration = ConfigurationCollector
				.collectConfiguration(context);
	}

	/**
	 * <p>
	 * Adds a custom key and value to be reported with the generated
	 * CashReportData.
	 * </p>
	 * <p>
	 * The key/value pairs will be stored in the "custom" column, as a text
	 * containing one 'key = value' pair on each line.
	 * </p>
	 * 
	 * @param key
	 *            A key for your custom data.
	 * @param value
	 *            The value associated to your key.
	 * @return The previous value for this key if there was one, or null.
	 */
	public String putCustomData(String key, String value) {
		return customParameters.put(key, value);
	}

	/**
	 * Removes a key/value pair from the custom data field.
	 * 
	 * @param key
	 *            The key of the data to be removed.
	 * @return The value for this key before removal.
	 */
	public String removeCustomData(String key) {
		return customParameters.remove(key);
	}

	/**
	 * Gets the current value for a key in the custom data field.
	 * 
	 * @param key
	 *            The key of the data to be retrieved.
	 * @return The value for this key.
	 */
	public String getCustomData(String key) {
		return customParameters.get(key);
	}

	/**
	 * Collects crash data.
	 * 
	 * @param th
	 *            Throwable that caused the crash.
	 * @param isSilentReport
	 *            Whether to report this report as being sent silently.
	 * @param brokenThread2
	 * @return CrashReportData representing the current state of the application
	 *         at the instant of the Exception.
	 */
	public CrashReportData createCrashData(Throwable th, Thread brokenThread) {
		final CrashReportData crashReportData = new CrashReportData();
		try {
			// Make every entry here bullet proof and move any slightly dodgy
			// ones to the end.
			// This ensures that we collect as much info as possible before
			// something crashes the collection process.

			crashReportData.put(STACK_TRACE, getStackTrace(th));
			crashReportData.put(ReportField.USER_APP_START_DATE,
					appStartDate.format3339(false));

			// Generate report uuid
			crashReportData.put(ReportField.REPORT_ID, UUID.randomUUID()
					.toString());

			// Installation unique ID
			crashReportData.put(INSTALLATION_ID,
					TFileInstallationUtils.id(context));

			// Device Configuration when crashing
			crashReportData.put(INITIAL_CONFIGURATION, initialConfiguration);

			crashReportData.put(CRASH_CONFIGURATION,
					ConfigurationCollector.collectConfiguration(context));

			crashReportData.put(DUMPSYS_MEMINFO,
					DumpSysCollector.collectMemInfo());

			// Application Package name
			crashReportData.put(PACKAGE_NAME, context.getPackageName());

			// Android OS Build details
			crashReportData.put(BUILD, ReflectionCollector
					.collectConstants(android.os.Build.class));

			// Device model
			crashReportData.put(PHONE_MODEL, android.os.Build.MODEL);

			// Android version
			crashReportData.put(ANDROID_VERSION,
					android.os.Build.VERSION.RELEASE);

			// Device Brand (manufacturer)
			crashReportData.put(BRAND, android.os.Build.BRAND);

			crashReportData.put(PRODUCT, android.os.Build.PRODUCT);

			// Device Memory
			crashReportData.put(TOTAL_MEM_SIZE,
					Long.toString(TStorageUtils.getTotalInternalMemorySize()));
			crashReportData.put(AVAILABLE_MEM_SIZE, Long.toString(TStorageUtils
					.getAvailableInternalMemorySize()));

			// Application file path
			TFilePath filePath = new TFilePath();
			crashReportData.put(FILE_PATH, filePath.getExternalAppDir());
			filePath = null;

			// Main display details
			crashReportData.put(DISPLAY,
					TScreenUtils.getDisplayDetails(context));

			// User crash date with local timezone
			final Time curDate = new Time();
			curDate.setToNow();
			crashReportData.put(USER_CRASH_DATE, curDate.format3339(false));

			// Add custom info, they are all stored in a single field
			crashReportData.put(CUSTOM_DATA, createCustomInfoString());

			// Device features
			crashReportData.put(DEVICE_FEATURES,
					DeviceFeaturesCollector.getFeatures(context));

			// Environment (External storage state)
			crashReportData.put(ENVIRONMENT, ReflectionCollector
					.collectStaticGettersResults(Environment.class));

			// System settings
			crashReportData.put(SETTINGS_SYSTEM,
					SettingsCollector.collectSystemSettings(context));

			// Secure settings
			crashReportData.put(SETTINGS_SECURE,
					SettingsCollector.collectSecureSettings(context));

			// SharedPreferences
			crashReportData.put(SHARED_PREFERENCES,
					SharedPreferencesCollector.collect(context));

			// Now get all the crash data that relies on the PackageManager
			// (which may or may not be here).
			final TPackageUtils pm = new TPackageUtils(context);

			final PackageInfo pi = pm.getPackageInfo();
			if (pi != null) {
				// Application Version
				crashReportData.put(APP_VERSION_CODE,
						Integer.toString(pi.versionCode));
				crashReportData.put(APP_VERSION_NAME,
						pi.versionName != null ? pi.versionName : "not set");
			} else {
				// Could not retrieve package info...
				crashReportData.put(APP_VERSION_NAME,
						"Package info unavailable");
			}

			// Retrieve UDID(IMEI) if permission is available
			if (pm.hasPermission(Manifest.permission.READ_PHONE_STATE)) {
				final String deviceId = TStorageUtils.getDeviceId(context);
				if (deviceId != null) {
					crashReportData.put(DEVICE_ID, deviceId);
				}
			}

			// Collect DropBox and logcat
			// Before JellyBean, this required the READ_LOGS permission
			// Since JellyBean, READ_LOGS is not granted to third-party apps
			// anymore for security reasons.
			// Though, we can call logcat without any permission and still get
			// traces related to our app.
			if ((pm.hasPermission(Manifest.permission.READ_LOGS))
					|| TAndroidVersionUtils.getAPILevel() >= 16) {
				TLog.i(TCrash.TAG,
						"READ_LOGS granted! ACRA can include LogCat and DropBox data.");
				// crashReportData
				// .put(LOGCAT, LogCatCollector.collectLogCat(null));
				// crashReportData.put(EVENTSLOG,
				// LogCatCollector.collectLogCat("events"));
				// crashReportData.put(RADIOLOG,
				// LogCatCollector.collectLogCat("radio"));
				// crashReportData.put(DROPBOX,
				// DropBoxCollector.read(context, null));
			} else {
				TLog.i(TCrash.TAG,
						"READ_LOGS not allowed. ACRA will not include LogCat and DropBox data.");
			}

			// Media Codecs list
			// crashReportData.put(MEDIA_CODEC_LIST,
			// MediaCodecListCollector.collecMediaCodecList());

			// Failing thread details
			crashReportData.put(THREAD_DETAILS,
					ThreadCollector.collect(brokenThread));

		} catch (RuntimeException e) {
			TLog.e(TAG, "Error while retrieving crash data" + e.getMessage());
		}

		return crashReportData;
	}

	/**
	 * Generates the string which is posted in the single custom data field in
	 * the GoogleDocs Form.
	 * 
	 * @return A string with a 'key = value' pair on each line.
	 */
	private String createCustomInfoString() {
		final StringBuilder customInfo = new StringBuilder();
		for (final String currentKey : customParameters.keySet()) {
			final String currentVal = customParameters.get(currentKey);
			customInfo.append(currentKey);
			customInfo.append(" = ");
			customInfo.append(currentVal);
			customInfo.append("\n");
		}
		return customInfo.toString();
	}

	private String getStackTrace(Throwable th) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable cause = th;
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		final String stacktraceAsString = result.toString();
		printWriter.close();

		return stacktraceAsString;
	}
}
