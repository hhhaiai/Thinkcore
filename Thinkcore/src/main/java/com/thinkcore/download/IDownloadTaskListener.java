package com.thinkcore.download;

import java.util.LinkedList;

public interface IDownloadTaskListener {
	public void onDownloadTaskUpdate(DownloadInfo task); // 更新进度

	public void onDownloadTaskCancel(DownloadInfo task); // 结束下载
}
