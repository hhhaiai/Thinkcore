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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.thinkcore.utils.log.TLog;

/**
 * 网络信息监听
 */
public class TTrafficInfo {
	private static final String TAG = TTrafficInfo.class.getSimpleName();

	private String uid;

	public TTrafficInfo(String uid) {
		this.uid = uid;
	}

	// 总的网络流量(上传和下载的总和)
	public long getSend() {
		RandomAccessFile rafSnd = null;
		String sndPath = "/proc/uid_stat/" + uid + "/tcp_snd";

		long sndTraffic = -1;
		try {
			rafSnd = new RandomAccessFile(sndPath, "r");
			sndTraffic = Long.parseLong(rafSnd.readLine());
		} catch (FileNotFoundException e) {
			sndTraffic = -1;
		} catch (NumberFormatException e) {
			TLog.e(TAG, "NumberFormatException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			TLog.e(TAG, "IOException: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rafSnd != null)
					rafSnd.close();
			} catch (IOException e) {
				TLog.i(TAG,
						"close randomAccessFile exception: " + e.getMessage());
			}
		}
		return sndTraffic;
	}

	public long getReceive() {
		RandomAccessFile rafRcv = null;
		String rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
		long rcvTraffic = -1;
		try {
			rafRcv = new RandomAccessFile(rcvPath, "r");
			rcvTraffic = Long.parseLong(rafRcv.readLine());
		} catch (FileNotFoundException e) {
			rcvTraffic = -1;
		} catch (NumberFormatException e) {
			TLog.e(TAG, "NumberFormatException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			TLog.e(TAG, "IOException: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rafRcv != null) {
					rafRcv.close();
				}
			} catch (IOException e) {
				TLog.i(TAG,
						"close randomAccessFile exception: " + e.getMessage());
			}
		}
		return rcvTraffic;
	}

	public long getTotal() {
		return getSend() + getReceive();
	}
}
