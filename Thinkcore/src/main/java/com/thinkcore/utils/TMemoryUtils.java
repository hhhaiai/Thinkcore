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
package com.thinkcore.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.thinkcore.utils.log.TLog;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

/**
 * 内存信息
 */
public class TMemoryUtils {
	private static final String TAG = TMemoryUtils.class.getSimpleName();

	/**
	 * 设备的总内存
	 */
	public static long getTotalMemory() {
		String memInfoPath = "/proc/meminfo";
		String readTemp = "";
		String memTotal = "";
		long memory = 0;
		try {
			FileReader fr = new FileReader(memInfoPath);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((readTemp = localBufferedReader.readLine()) != null) {
				if (readTemp.contains("MemTotal")) {
					String[] total = readTemp.split(":");
					memTotal = total[1].trim();
				}
			}
			localBufferedReader.close();
			String[] memKb = memTotal.split(" ");
			memTotal = memKb[0].trim();
			TLog.d(TAG, "memTotal: " + memTotal);
			memory = Long.parseLong(memTotal);
		} catch (IOException e) {
			TLog.e(TAG, "IOException: " + e.getMessage());
		}
		return memory;
	}

	// 获取可用内存
	public static long getFreeMemorySize(Context context) {
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(outInfo);
		long avaliMem = outInfo.availMem;
		return avaliMem / 1024;
	}

	// 获取指定进程内存
	public static int getPidMemorySize(int pid, Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int[] myMempid = new int[] { pid };
		Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
		memoryInfo[0].getTotalSharedDirty();

		// int memSize = memoryInfo[0].dalvikPrivateDirty;
		int memSize = memoryInfo[0].getTotalPss();
		// int memSize = memoryInfo[0].getTotalPrivateDirty();
		return memSize;
	}
}
