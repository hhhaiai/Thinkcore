package com.thinkcore.download;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

import com.thinkcore.TIGlobalInterface;
import com.thinkcore.storage.TStorageUtils;

public class TDownloadManager implements TIGlobalInterface {
	public final static int ERROR_NONE = 0;
	public final static int ERROR_SD_NO_MEMORY = 1;
	public final static int ERROR_BLOCK_INTERNET = 2;
	public final static int ERROR_UNKONW = 3;
	public final static int TIME_OUT = 30000;
	public final static int BUFFER_SIZE = 1024 * 8;

	private LinkedList<DownloadInfo> mTaskQueue = new LinkedList<DownloadInfo>();
	private LinkedList<IDownloadTaskListener> mIDownloadTaskListeners = new LinkedList<IDownloadTaskListener>();;

	private static TDownloadManager mThis = null;
	private Timer mTaskTimer;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateDownloadTask();
			super.handleMessage(msg);
		}
	};

	public TDownloadManager() {

	}

	public static TDownloadManager getInstance() {
		if (mThis == null)
			mThis = new TDownloadManager();
		return mThis;
	}

	@Override
	public void initConfig() {

	}

	@Override
	public void release() {
		getTaskQueue().clear();
		getTIDownloadTaskListener().clear();

		if (mTaskTimer != null) {
			mTaskTimer.cancel();
			mTaskTimer = null;
		}
	}

	public synchronized LinkedList<DownloadInfo> getTaskQueue() {
		return mTaskQueue;
	}

	public long addDownloadTask(DownloadInfo downloadInfo) throws Exception {// 添加
		if (!TStorageUtils.isExternalStoragePresent())
			throw new Exception("未发现SD卡");
		if (!TStorageUtils.isExternalStorageWrittenable())
			throw new Exception("SD卡不能读写");

		if (downloadInfo.getId() != 0 && hasDownloadTask(downloadInfo.getId())) {
			downloadInfo.setManager(this);
			downloadInfo.startTask();
			return downloadInfo.getId();
		}

		getTaskQueue().add(downloadInfo);
		downloadInfo.setManager(this);
		downloadInfo.startTask();

		if (mTaskTimer == null) {
			mTaskTimer = new Timer();
			mTaskTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					mHandler.sendEmptyMessage(0);

					if (getTaskQueue().isEmpty()) {
						mTaskTimer.cancel();
						mTaskTimer = null;
					}
				}
			}, 1000, 1000);
		}
		return downloadInfo.getId();
	}

	public boolean delDownloadTask(long id) throws Exception {// 删除
		if (id == 0)
			throw new Exception("id = 0");
		for (DownloadInfo info : getTaskQueue()) {
			if (info.getId() == id)
				return getTaskQueue().remove(info);
		}

		return false;
	}

	private boolean delDownloadTask(DownloadInfo downloadInfo) {// 删除
		if (downloadInfo == null)
			return false;

		downloadInfo.stopTask();
		return getTaskQueue().remove(downloadInfo);
	}

	private void stopDownloadTask(DownloadInfo downloadInfo) throws Exception {// 停止
		if (downloadInfo == null || downloadInfo.getId() == 0)
			throw new Exception("null");
		downloadInfo.stopTask();
	}

	public void stopDownloadTask(long id) throws Exception {// 停止
		if (id == 0)
			throw new Exception("id = 0");
		for (DownloadInfo info : getTaskQueue()) {
			if (info.getId() == id) {
				stopDownloadTask(info);
				return;
			}
		}
	}

	public synchronized void updateDownloadTask() {// 更新
		if (getTIDownloadTaskListener().isEmpty())
			return;

		for (DownloadInfo info : getTaskQueue()) {
			for (IDownloadTaskListener listener : getTIDownloadTaskListener()) {
				listener.onDownloadTaskUpdate(info);
			}
		}
	}

	public void onUpdateDownloadTask(DownloadInfo info) {
		if (getTIDownloadTaskListener().isEmpty())
			return;
		for (IDownloadTaskListener listener : getTIDownloadTaskListener()) {
			listener.onDownloadTaskUpdate(info);
		}
	}

	public void onCancelDownloadTask(DownloadInfo info) {
		if (getTIDownloadTaskListener().isEmpty())
			return;
		for (IDownloadTaskListener listener : getTIDownloadTaskListener()) {
			listener.onDownloadTaskCancel(info);
		}
	}

	public boolean hasDownloadTask(long id) {
		for (DownloadInfo info : getTaskQueue()) {
			if (info.getId() == id)
				return true;
		}

		return false;
	}

	public synchronized LinkedList<IDownloadTaskListener> getTIDownloadTaskListener() {
		return mIDownloadTaskListeners;
	}

	public void addIDownloadTaskListener(IDownloadTaskListener listener) {
		getTIDownloadTaskListener().add(listener);
	}

	public void removeIDownloadTaskListener(IDownloadTaskListener listener) {
		getTIDownloadTaskListener().remove(listener);
	}
}
