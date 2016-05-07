/*
 *  Copyright 2010 Kevin Gaudin
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
package com.treecore.crash.collector;

import com.treecore.crash.TCrash;
import com.treecore.utils.TAndroidVersionUtils;
import com.treecore.utils.log.TLog;
import com.treecore.utils.stl.BoundedLinkedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.treecore.crash.TCrash.TAG;

/**
 * 命令的输出信息
 */
final class LogCatCollector {// modify

	/**
	 * Default number of latest lines kept from the logcat output.
	 */
	private static final int DEFAULT_TAIL_COUNT = 100;

	/**
	 * Executes the logcat command with arguments taken from
	 * {@link ReportsCrashes#logcatArguments()}
	 * 
	 * @param bufferName
	 *            The name of the buffer to be read: "main" (default), "radio"
	 *            or "events".
	 * @return A {@link String} containing the latest lines of the output.
	 *         Default is 100 lines, use "-t", "300" in
	 *         {@link ReportsCrashes#logcatArguments()} if you want 300 lines.
	 *         You should be aware that increasing this value causes a longer
	 *         report generation time and a bigger footprint on the device data
	 *         plan consumption.
	 */
	public static String collectLogCat(String bufferName) {
		final int myPid = android.os.Process.myPid();
		String myPidStr = null;
		// if (TAcra.getConfig().logcatFilterByPid() && myPid > 0) {
		myPidStr = Integer.toString(myPid) + "):";
		// }

		final List<String> commandLine = new ArrayList<String>();
		commandLine.add("logcat");
		if (bufferName != null) {
			commandLine.add("-b");
			commandLine.add(bufferName);
		}

		// "-t n" argument has been introduced in FroYo (API level 8). For
		// devices with lower API level, we will have to emulate its job.
		final int tailCount;
		final List<String> logcatArgumentsList = new ArrayList<String>();
		// Arrays.asList(TAcra.getConfig().logcatArguments())

		final int tailIndex = logcatArgumentsList.indexOf("-t");
		if (tailIndex > -1 && tailIndex < logcatArgumentsList.size()) {
			tailCount = Integer
					.parseInt(logcatArgumentsList.get(tailIndex + 1));
			if (TAndroidVersionUtils.getAPILevel() < 8) {
				logcatArgumentsList.remove(tailIndex + 1);
				logcatArgumentsList.remove(tailIndex);
				logcatArgumentsList.add("-d");
			}
		} else {
			tailCount = -1;
		}

		final LinkedList<String> logcatBuf = new BoundedLinkedList<String>(
				tailCount > 0 ? tailCount : DEFAULT_TAIL_COUNT);
		commandLine.addAll(logcatArgumentsList);

		try {
			final Process process = Runtime.getRuntime().exec(
					commandLine.toArray(new String[commandLine.size()]));
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()),
					TCrash.DEFAULT_BUFFER_SIZE_IN_BYTES);

			TLog.d(TAG, "Retrieving logcat output...");
			while (true) {
				final String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				if (myPidStr == null || line.contains(myPidStr)) {
					logcatBuf.add(line + "\n");
				}
			}

		} catch (IOException e) {
			TLog.e(TCrash.TAG,
					"LogCatCollector.collectLogCat could not retrieve data."
							+ e.getMessage());
		}

		return logcatBuf.toString();
	}
}
