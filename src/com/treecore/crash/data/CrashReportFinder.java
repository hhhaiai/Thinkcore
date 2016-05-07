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
package com.treecore.crash.data;

import java.io.File;
import java.io.FilenameFilter;

import com.treecore.crash.TCrash;
import com.treecore.utils.log.TLog;

/**
 * 报告文件查找
 */
public class CrashReportFinder {

	public CrashReportFinder() {
	}

	/**
	 * Returns an array containing the names of pending crash report files.
	 * 
	 * @return an array containing the names of pending crash report files.
	 */
	public String[] getCrashReportFiles() {
		final File dir = new File(TCrash.getInstance().getFilePath());
		if (dir == null) {
			TLog.w(TCrash.TAG,
					"Application files directory does not exist! The application may not be installed correctly. Please try reinstalling.");
			return new String[0];
		}

		TLog.d(TCrash.TAG, "Looking for error files in " + dir.getAbsolutePath());

		// Filter for ".stacktrace" files
		final FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(TCrash.REPORTFILE_EXTENSION);
			}
		};
		final String[] result = dir.list(filter);
		return (result == null) ? new String[0] : result;
	}
}
