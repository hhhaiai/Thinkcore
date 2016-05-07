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
package com.treecore.crash;

import static com.treecore.crash.TCrash.TAG;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.treecore.crash.data.CrashReportData;
import com.treecore.crash.data.CrashReportFileNameParser;
import com.treecore.crash.data.CrashReportFinder;
import com.treecore.crash.data.TCrashReportPersister;
import com.treecore.crash.exception.ReportSenderException;
import com.treecore.utils.log.TLog;

import android.content.Context;

/**
 * 检查和发送一个报告
 */
final class SendWorker extends Thread {
	private final boolean approvePendingReports;
	private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();

	/**
	 * Creates a new {@link SendWorker} to try sending pending reports.
	 * 
	 * @param context
	 *            ApplicationContext in which the reports are being sent.
	 * @param reportSenders
	 *            List of ReportSender to use to send the crash reports.
	 * @param sendOnlySilentReports
	 *            If set to true, will send only reports which have been
	 *            explicitly declared as silent by the application developer.
	 * @param approvePendingReports
	 *            if this endWorker should approve pending reports before
	 *            sending any reports.
	 */
	public SendWorker(boolean approvePendingReports) {
		this.approvePendingReports = approvePendingReports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (approvePendingReports) {
			approvePendingReports();
		}
		checkAndSendReports();
	}

	/**
	 * Flag all pending reports as "approved" by the user. These reports can be
	 * sent.
	 */
	private void approvePendingReports() {
		TLog.d(TAG, "Mark all pending reports as approved.");

		final CrashReportFinder reportFinder = new CrashReportFinder();
		final String[] reportFileNames = reportFinder.getCrashReportFiles();

		for (String reportFileName : reportFileNames) {
			if (!fileNameParser.isApproved(reportFileName)) {
				final File reportFile = new File(TCrash.getInstance()
						.getFilePath(), reportFileName);

				// TODO look into how this could cause a file to go from
				// -approved.stacktrace to -approved-approved.stacktrace
				final String newName = reportFileName.replace(
						TCrash.REPORTFILE_EXTENSION, TCrash.APPROVED_SUFFIX
								+ TCrash.REPORTFILE_EXTENSION);

				// TODO Look into whether rename is atomic. Is there a better
				// option?
				final File newFile = new File(
						TCrash.getInstance().getFilePath(), newName);
				if (!reportFile.renameTo(newFile)) {
					TLog.e(TAG, "Could not rename approved report from "
							+ reportFile + " to " + newFile);
				}
			}
		}
	}

	/**
	 * Send pending reports.
	 * 
	 * @param context
	 *            The application context.
	 * @param sendOnlySilentReports
	 *            Send only reports explicitly declared as SILENT by the
	 *            developer (sent via
	 *            {@link TCrashErrorReporter#handleSilentException(Throwable)}.
	 */
	private void checkAndSendReports() {
		TLog.d(TAG, "#checkAndSendReports - start");
		final CrashReportFinder reportFinder = new CrashReportFinder();
		final String[] reportFiles = reportFinder.getCrashReportFiles();
		Arrays.sort(reportFiles);

		int reportsSentCount = 0;

		for (String curFileName : reportFiles) {
			if (reportsSentCount > 1) {
				deleteFile(curFileName);
				continue;
			}

			TLog.i(TAG, "Sending file " + curFileName);
			try {
				final TCrashReportPersister persister = new TCrashReportPersister();
				final CrashReportData previousCrashReport = persister
						.load(curFileName);
				sendCrashReport(previousCrashReport);
				deleteFile(curFileName);
			} catch (RuntimeException e) {
				TLog.e(TCrash.TAG, "Failed to send crash reports for "
						+ curFileName + e.getMessage());
				deleteFile(curFileName);
				break; // Something really unexpected happened. Don't try to
						// send any more reports now.
			} catch (IOException e) {
				TLog.e(TCrash.TAG, "Failed to load crash report for "
						+ curFileName + e.getMessage());
				deleteFile(curFileName);
				break; // Something unexpected happened when reading the crash
						// report. Don't try to send any more reports now.
			} catch (ReportSenderException e) {
				TLog.e(TCrash.TAG, "Failed to send crash report for "
						+ curFileName + e.getMessage());
				break; // Something stopped the report being sent. Don't try to
						// send any more reports now.
			}
			reportsSentCount++;
		}
		TLog.d(TAG, "#checkAndSendReports - finish");
	}

	/**
	 * Sends the report with all configured ReportSenders. If at least one
	 * sender completed its job, the report is considered as sent and will not
	 * be sent again for failing senders.
	 * 
	 * @param errorContent
	 *            Crash data.
	 * @throws ReportSenderException
	 *             if unable to send the crash report.
	 */
	private void sendCrashReport(CrashReportData errorContent)
			throws ReportSenderException {
		boolean sentAtLeastOnce = false;
		for (TIReportSender sender : TCrash.getInstance().getReportSenders()) {
			try {
				sender.send(errorContent);
				// 只要有一个发送成功，异常则不据需
				sentAtLeastOnce = true;
			} catch (ReportSenderException e) {
				if (!sentAtLeastOnce) {
					throw e; //
				} else {
					TLog.w(TAG,
							"ReportSender of class "
									+ sender.getClass().getName()
									+ " failed but other senders completed their task. ACRA will not send this report again.");
				}
			}
		}
	}

	private void deleteFile(String fileName) {
		final boolean deleted = TCrash.getInstance().getContext()
				.deleteFile(fileName);
		if (!deleted) {
			TLog.w(TCrash.TAG, "Could not delete error report : " + fileName);
		}
	}
}
