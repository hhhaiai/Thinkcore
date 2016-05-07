package com.treecore.crash.data;

import com.treecore.crash.TCrash;
import com.treecore.crash.TCrashErrorReporter;

/**
 * 负责确定一个基于其文件名事故报告状态
 */
public class CrashReportFileNameParser {

	/**
	 * Guess that a report is silent from its file name.
	 * 
	 * @param reportFileName
	 *            Name of the report to check whether it should be sent
	 *            silently.
	 * @return True if the report has been declared explicitly silent using
	 *         {@link TCrashErrorReporter#handleSilentException(Throwable)}.
	 */
	public boolean isSilent(String reportFileName) {
		return reportFileName.contains(TCrash.SILENT_SUFFIX);
	}

	/**
	 * Returns true if the report is considered as approved.
	 * <p>
	 * This includes:
	 * </p>
	 * <ul>
	 * <li>Reports which were pending when the user agreed to send a report in
	 * the NOTIFICATION mode Dialog.</li>
	 * <li>Explicit silent reports</li>
	 * </ul>
	 * 
	 * @param reportFileName
	 *            Name of report to check whether it is approved to be sent.
	 * @return True if a report can be sent.
	 */
	public boolean isApproved(String reportFileName) {
		return isSilent(reportFileName)
				|| reportFileName.contains(TCrash.APPROVED_SUFFIX);
	}
}
