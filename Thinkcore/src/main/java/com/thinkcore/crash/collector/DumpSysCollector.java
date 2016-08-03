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
package com.thinkcore.crash.collector;

import com.thinkcore.crash.TCrash;
import com.thinkcore.utils.log.TLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码命令信息
 */
final class DumpSysCollector {

	/**
	 * Collect results of the <code>dumpsys meminfo</code> command restricted to
	 * this application process.
	 * 
	 * @return The execution result.
	 */
	public static String collectMemInfo() {

		final StringBuilder meminfo = new StringBuilder();
		try {
			final List<String> commandLine = new ArrayList<String>();
			commandLine.add("dumpsys");
			commandLine.add("meminfo");
			commandLine.add(Integer.toString(android.os.Process.myPid()));

			final Process process = Runtime.getRuntime().exec(
					commandLine.toArray(new String[commandLine.size()]));
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()),
					TCrash.DEFAULT_BUFFER_SIZE_IN_BYTES);

			while (true) {
				final String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				meminfo.append(line);
				meminfo.append("\n");
			}

		} catch (IOException e) {
			TLog.e(TCrash.TAG,
					"DumpSysCollector.meminfo could not retrieve data"
							+ e.getMessage());
		}

		return meminfo.toString();
	}
}