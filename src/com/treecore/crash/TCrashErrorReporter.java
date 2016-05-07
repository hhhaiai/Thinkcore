package com.treecore.crash;

import java.io.File;
import java.util.Arrays;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.Time;
import android.widget.Toast;

import com.treecore.crash.collector.CrashReportDataFactory;
import com.treecore.crash.data.CrashReportData;
import com.treecore.crash.data.CrashReportFileNameParser;
import com.treecore.crash.data.CrashReportFinder;
import com.treecore.crash.data.TCrashReportPersister;
import com.treecore.utils.TToastUtils;
import com.treecore.utils.log.TLog;

import static com.treecore.crash.TCrash.TAG;
import static com.treecore.crash.data.ReportField.IS_SILENT;

/**
 * 错误信息收集
 */
public class TCrashErrorReporter implements Thread.UncaughtExceptionHandler {
	private final CrashReportDataFactory mCrashReportDataFactory; // 错误数据生成
	private final CrashReportFileNameParser mFileNameParser = new CrashReportFileNameParser(); // 文件名过滤

	private final Thread.UncaughtExceptionHandler mDfltExceptionHandler;// 系统默认异常处理

	private Thread mBrokenThread;
	private Throwable mUnhandledThrowable;

	private static boolean mToastWaitEnded = true;// 是否等待

	public TCrashErrorReporter() {
		mCrashReportDataFactory = new CrashReportDataFactory(TCrash
				.getInstance().getContext());

		mDfltExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		checkReportsOnApplicationStart();// 检测未处理的报告
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
	 * .Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		try {
			if (!TCrash.getInstance().isEnable()) {// 需要处理则处理
				if (mDfltExceptionHandler != null) {
					TLog.e(TCrash.TAG,
							"ACRA is disabled for "
									+ TCrash.getInstance().getContext()
											.getPackageName()
									+ " - forwarding uncaught Exception on to default ExceptionHandler");
					mDfltExceptionHandler.uncaughtException(t, e);
				} else {
					TLog.e(TCrash.TAG, "ACRA is disabled for "
							+ TCrash.getInstance().getContext()
									.getPackageName()
							+ " - no default ExceptionHandler");
				}
				return;
			}

			mBrokenThread = t;
			mUnhandledThrowable = e;

			TLog.e(TCrash.TAG, "ACRA caught a " + e.getClass().getSimpleName()
					+ " exception for "
					+ TCrash.getInstance().getContext().getPackageName()
					+ ". Building report.");

			// 生成发送错误报告
			handleException(e, true);
		} catch (Throwable fatality) {
			// 异常则调用本地
			if (mDfltExceptionHandler != null) {
				mDfltExceptionHandler.uncaughtException(t, e);
			}
		}
	}

	/**
	 * 结束应用
	 */
	private void endApplication() {
		// if (TAcra.getInstance().isEnable()) {
		// mDfltExceptionHandler.uncaughtException(mBrokenThread,
		// mUnhandledThrowable);
		// } else {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
		// }
	}

	/**
	 * 开启线程发送错误报告
	 */
	public SendWorker startSendingReports(boolean approveReportsFirst) {
		final SendWorker worker = new SendWorker(approveReportsFirst);
		worker.start();
		return worker;
	}

	/**
	 * 删除所有的报告文件
	 */
	public void deletePendingReports() {
		deletePendingReports(true, true, 0);
	}

	/**
	 * 检测未处理的报告
	 */
	private void checkReportsOnApplicationStart() {
		deletePendingNonApprovedReports(true);// 删除所有未批准的项目

		final CrashReportFinder reportFinder = new CrashReportFinder();
		String[] filesList = reportFinder.getCrashReportFiles();

		if (filesList != null && filesList.length > 0) {
			filesList = reportFinder.getCrashReportFiles();
			// final boolean onlySilentOrApprovedReports =
			// containsOnlySilentOrApprovedReports(filesList);
			if (TCrash.getInstance().getICarshListener() != null) {
				TCrash.getInstance().getICarshListener()
						.onAppCrash(getLatestNonSilentReport(filesList));
			}
		}
	}

	/**
	 * 删除所有未未经批准的报告
	 */
	public void deletePendingNonApprovedReports(boolean keepOne) {
		// 抱持最新的报告
		final int nbReportsToKeep = keepOne ? 1 : 0;
		deletePendingReports(false, true, nbReportsToKeep);
	}

	/**
	 * 尝试发送报告，如果失败则存储
	 */
	private void handleException(Throwable e, final boolean endApplication) {
		if (!TCrash.getInstance().isEnable()) {// 不可用
			return;
		}

		if (e == null) {
			e = new Exception("Report requested by developer");
		}

		final CrashReportData crashReportData = mCrashReportDataFactory
				.createCrashData(e, mBrokenThread); // 生成错误报告

		if (!TextUtils.isEmpty(TCrash.getInstance().getCrashContentShow())) {
			new Thread() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					Looper.prepare();
					TToastUtils.makeText(TCrash.getInstance()
							.getCrashContentShow(), Toast.LENGTH_LONG);
					Looper.loop();
				}

			}.start();
		}

		// 写报告文件
		final String reportFileName = getReportFileName(crashReportData);
		saveCrashReportFile(reportFileName, crashReportData);

		SendWorker sender = null;

		mToastWaitEnded = false;
		new Thread() {

			@Override
			public void run() {
				final Time beforeWait = new Time();
				final Time currentTime = new Time();
				beforeWait.setToNow();
				final long beforeWaitInMillis = beforeWait.toMillis(false);
				long elapsedTimeInMillis = 0;
				while (elapsedTimeInMillis < 3000) {
					try {
						// Wait a bit to let the user read the toast
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						TLog.d(TAG,
								"Interrupted while waiting for Toast to end."
										+ e1.getMessage());
					}
					currentTime.setToNow();
					elapsedTimeInMillis = currentTime.toMillis(false)
							- beforeWaitInMillis;
				}
				mToastWaitEnded = true;
			}
		}.start();

		// 调用对话框
		final SendWorker worker = sender;

		new Thread() {

			@Override
			public void run() {
				// We have to wait for BOTH the toast display wait AND
				// the worker job to be completed.
				TLog.d(TAG, "Waiting for Toast + worker...");
				while (!mToastWaitEnded || (worker != null && worker.isAlive())) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						TLog.e(TAG, "Error : " + e1.getMessage());
					}
				}

				if (TCrash.getInstance().getICarshListener() != null) {
					TCrash.getInstance().getICarshListener()
							.onAppCrash(reportFileName);
				}

				// notifyDialog();
				if (endApplication) {
					endApplication();
				}
			}
		}.start();
	}

	// 获取文件名
	private String getReportFileName(CrashReportData crashData) {
		final Time now = new Time();
		now.setToNow();
		final long timestamp = now.toMillis(false);
		final String isSilent = crashData.getProperty(IS_SILENT);
		return "" + timestamp + TCrash.REPORTFILE_EXTENSION;
	}

	/**
	 * 无法发送则保存
	 */
	private void saveCrashReportFile(String fileName, CrashReportData crashData) {
		try {
			TLog.d(TAG, "Writing crash report file " + fileName + ".");
			final TCrashReportPersister persister = new TCrashReportPersister();
			persister.store(crashData, fileName);
		} catch (Exception e) {
			TLog.e(TAG, "An error occurred while writing the report file..."
					+ e.getMessage());
		}
	}

	/**
	 * 获取最近未处理的报告
	 */
	private String getLatestNonSilentReport(String[] filesList) {
		if (filesList != null && filesList.length > 0) {
			for (int i = filesList.length - 1; i >= 0; i--) {
				if (!mFileNameParser.isSilent(filesList[i])) {
					return filesList[i];
				}
			}
			// We should never have this result, but this should be secure...
			return filesList[filesList.length - 1];
		} else {
			return null;
		}
	}

	/**
	 * 删除搁置报告
	 */
	private void deletePendingReports(boolean deleteApprovedReports,
			boolean deleteNonApprovedReports, int nbOfLatestToKeep) {
		// 检查
		final CrashReportFinder reportFinder = new CrashReportFinder();
		final String[] filesList = reportFinder.getCrashReportFiles();
		Arrays.sort(filesList);
		if (filesList != null) {
			for (int iFile = 0; iFile < filesList.length - nbOfLatestToKeep; iFile++) {
				final String fileName = filesList[iFile];
				final boolean isReportApproved = mFileNameParser
						.isApproved(fileName);
				if ((isReportApproved && deleteApprovedReports)
						|| (!isReportApproved && deleteNonApprovedReports)) {
					final File fileToDelete = new File(TCrash.getInstance()
							.getFilePath(), fileName);
					if (!fileToDelete.delete()) {
						TLog.e(TCrash.TAG, "Could not delete report : "
								+ fileToDelete);
					}
				}
			}
		}
	}

	/**
	 * 检查报告文件是否包含……
	 */
	private boolean containsOnlySilentOrApprovedReports(String[] reportFileNames) {
		for (String reportFileName : reportFileNames) {
			if (!mFileNameParser.isApproved(reportFileName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 放入自定义数据
	 */
	@Deprecated
	public void addCustomData(String key, String value) {
		mCrashReportDataFactory.putCustomData(key, value);
	}

	/**
	 * 放入自定义数据
	 */
	public String putCustomData(String key, String value) {
		return mCrashReportDataFactory.putCustomData(key, value);
	}

	/**
	 * 移除指定数据
	 */
	public String removeCustomData(String key) {
		return mCrashReportDataFactory.removeCustomData(key);
	}

	/**
	 * 获取数据
	 */
	public String getCustomData(String key) {
		return mCrashReportDataFactory.getCustomData(key);
	}
}
