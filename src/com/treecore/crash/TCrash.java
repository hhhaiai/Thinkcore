/*
 *  Copyright 2010 Emmanuel Astier & Kevin Gaudin
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
package com.treecore.crash;

import java.util.ArrayList;
import java.util.List;

import com.treecore.TIGlobalInterface;
import com.treecore.activity.TActivity;
import com.treecore.crash.data.ReportField;
import com.treecore.crash.data.ReportingInteractionMode;
import com.treecore.utils.log.TLog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

/**
 * 奔溃管理
 */
public class TCrash implements TIGlobalInterface {
	public static final String TAG = TCrash.class.getSimpleName();

	public static final String REPORTFILE_EXTENSION = ".stacktrace";
	public static final String APPROVED_SUFFIX = "-approved";// 已批准
	public static final String EXTRA_REPORT_FILE_NAME = "REPORT_FILE_NAME";// 导出文件名
	public static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 8192;
	public static final String SILENT_SUFFIX = "-" + ReportField.IS_SILENT;

	private Context mContext;
	private boolean mEnabled = true;// 默认开启

	private TCrashErrorReporter mTAcraErrorReporter;
	private String mFilePath = "";
	private final List<TIReportSender> mReportSenders = new ArrayList<TIReportSender>();// 发送接口
	private static TCrash mThis;
	private TICrashListener mCrashListener;
	private String mAppCrashContentString = "";

	public static TCrash getInstance() {
		if (mThis == null)
			mThis = new TCrash();
		return mThis;
	}

	public void initConfig(Context context) {
		mContext = context;
		initConfig();
	}

	@Override
	public void initConfig() {
		try {
			mTAcraErrorReporter = new TCrashErrorReporter();
		} catch (Exception e) {
			TLog.w(TAG, "Error : " + e.getMessage());
		}
	}

	@Override
	public void release() {
	}

	public TCrashErrorReporter getErrorReporter() {
		if (mTAcraErrorReporter == null) {
			throw new IllegalStateException(
					"Cannot access ErrorReporter before ACRA#init");
		}
		return mTAcraErrorReporter;
	}

	public void setFilePath(String path) {
		mFilePath = path;
	}

	public String getFilePath() {
		if (TextUtils.isEmpty(mFilePath))
			return mContext.getFilesDir().getAbsolutePath();
		return mFilePath;
	}

	public Context getContext() {
		return mContext;
	}

	public List<TIReportSender> getReportSenders() {
		return mReportSenders;
	}

	/**
	 * Add a {@link TIReportSender} to the list of active {@link TIReportSender}
	 * s.
	 * 
	 * @param sender
	 *            The {@link TIReportSender} to be added.
	 */
	public void addReportSender(TIReportSender sender) {
		mReportSenders.add(sender);
	}

	/**
	 * Remove a specific instance of {@link TIReportSender} from the list of
	 * active {@link TIReportSender}s.
	 * 
	 * @param sender
	 *            The {@link TIReportSender} instance to be removed.
	 */
	public void removeReportSender(TIReportSender sender) {
		mReportSenders.remove(sender);
	}

	/**
	 * Remove all {@link TIReportSender} instances from a specific class.
	 * 
	 * @param senderClass
	 *            ReportSender class whose instances should be removed.
	 */
	public void removeReportSenders(Class<?> senderClass) {
		if (TIReportSender.class.isAssignableFrom(senderClass)) {
			for (TIReportSender sender : mReportSenders) {
				if (senderClass.isInstance(sender)) {
					mReportSenders.remove(sender);
				}
			}
		}
	}

	/**
	 * Clears the list of active {@link TIReportSender}s. You should then call
	 * {@link #addReportSender(TIReportSender)} or ACRA will not send any report
	 * anymore.
	 */
	public void removeAllReportSenders() {
		mReportSenders.clear();
	}

	/**
	 * Removes all previously set {@link TIReportSender}s and set the given one
	 * as the new {@link TIReportSender}.
	 * 
	 * @param sender
	 *            ReportSender to set as the sole sender for this ErrorReporter.
	 */
	public void setReportSender(TIReportSender sender) {
		removeAllReportSenders();
		addReportSender(sender);
	}

	public void setEnable(boolean enable) {
		mEnabled = enable;
	}

	public boolean isEnable() {
		return mEnabled;
	}

	public void setICrashListener(TICrashListener listener) {
		mCrashListener = listener;
	}

	public TICrashListener getICarshListener() {
		return mCrashListener;
	}

	public void setCrashContentShow(String content) {
		mAppCrashContentString = content;
	}

	public String getCrashContentShow() {
		return mAppCrashContentString;
	}

	public ReportField[] getReportFields() {
		return mReportFields;
	}

	ReportField[] mReportFields = { ReportField.REPORT_ID,
			ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
			ReportField.PACKAGE_NAME, ReportField.FILE_PATH,
			ReportField.PHONE_MODEL, ReportField.ANDROID_VERSION,
			ReportField.BUILD, ReportField.BRAND, ReportField.PRODUCT,
			ReportField.TOTAL_MEM_SIZE, ReportField.AVAILABLE_MEM_SIZE,
			ReportField.CUSTOM_DATA, ReportField.STACK_TRACE,
			ReportField.INITIAL_CONFIGURATION, ReportField.CRASH_CONFIGURATION,
			ReportField.DISPLAY, ReportField.USER_COMMENT,
			ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE,
			ReportField.DUMPSYS_MEMINFO, ReportField.DROPBOX,
			ReportField.LOGCAT, ReportField.EVENTSLOG, ReportField.RADIOLOG,
			ReportField.IS_SILENT, ReportField.DEVICE_ID,
			ReportField.INSTALLATION_ID, ReportField.USER_EMAIL,
			ReportField.DEVICE_FEATURES, ReportField.ENVIRONMENT,
			ReportField.SETTINGS_SYSTEM, ReportField.SETTINGS_SECURE,
			ReportField.SHARED_PREFERENCES, ReportField.APPLICATION_LOG,
			ReportField.MEDIA_CODEC_LIST, ReportField.THREAD_DETAILS };
}
