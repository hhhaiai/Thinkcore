package com.thinkcore.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.thinkcore.storage.TStorageUtils;
import com.thinkcore.utils.TFileUtils;
import com.thinkcore.utils.log.TLog;
import com.thinkcore.utils.network.TNetWorkUtil;
import com.thinkcore.utils.task.TITaskListener;
import com.thinkcore.utils.task.TTask;
import com.thinkcore.utils.task.TTask.Task;
import com.thinkcore.utils.task.TTask.TaskEvent;

public class DownloadInfo implements TITaskListener {
	private String TAG = DownloadInfo.class.getSimpleName();
	private String mUrl = "";
	private String mFilePath = "", mFileName = "";

	private long mId = 0;
	private long mPreviousFileSize = 0;// 文件起始大小
	private long mTotalSize = 100;
	private long mDownloadSize = 0;
	private long mSpeed = 0; // 网速
	private long mStartTime = 0; // 起始时间
	private long mTotalTime = 0;
	private int mErrorCode = TDownloadManager.ERROR_NONE;
	private TTask mDownloadTask;

	private Throwable mException;
	private AndroidHttpClient mAndroidHttpClient = null;
	private RandomAccessFile mRandomAccessFile;
	private byte[] mBuffer;
	private static int ICount = 1;
	private String mFileMd5 = "";

	public DownloadInfo(String url, String filePath, String fileName) {
		mUrl = url;
		mFilePath = filePath;
		mFileName = fileName;
		mId = ICount++;
	}

	public DownloadInfo(String url, String filePath, String fileName, String md5) {
		mUrl = url;
		mFilePath = filePath;
		mFileName = fileName;
		mId = ICount++;
		mFileMd5 = md5;

		File file = new File(mFilePath, mFileName);
		if (file.exists()
				&& !TFileUtils.getFileMD5(
						mFilePath + File.separator + mFileName).equals(md5)) {
			file.delete();
		}
	}

	public void setManager(TDownloadManager manager) {
	}

	public long getId() {
		return mId;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getFileName() {
		return mFileName;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public long getTotalSize() {
		return mTotalSize;
	}

	public long getDownloadSize() {
		return mDownloadSize;
	}

	public long getSpeed() {
		return mSpeed;
	}

	public long getStartTime() {
		return mStartTime;
	}

	public long getPercent() {
		return (mDownloadSize + mPreviousFileSize) * 100 / mTotalSize;
	}

	public long getTotalTime() {
		return mTotalTime;
	}

	public void setErrorCode(int error) {
		mErrorCode = error;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public Throwable getError() {
		return mException;
	}

	public boolean isRuning() {
		return mDownloadTask != null && !mDownloadTask.getTask().isCancel();
	}

	public void startTask() {
		if (mDownloadTask == null) {
			mDownloadTask = new TTask();
			mDownloadTask.setIXTaskListener(this);
		}

		mErrorCode = TDownloadManager.ERROR_NONE;
		mDownloadTask.startTask(0);
		mStartTime = System.currentTimeMillis();
	}

	public void stopTask() {
		TDownloadManager.getInstance().onCancelDownloadTask(this);

		if (mDownloadTask != null) {
			mDownloadTask.stopTask();
			mDownloadTask = null;
		}

		if (mAndroidHttpClient != null) {
			mAndroidHttpClient.close();
		}

		try {
			if (mRandomAccessFile != null) {
				mRandomAccessFile.close();
				mRandomAccessFile = null;
			}
		} catch (Exception e) {
		}

		if (mBuffer != null) {
			mBuffer = null;
		}

		TDownloadManager.getInstance().onCancelDownloadTask(this);
	}

	@Override
	public void onTask(Task task, TaskEvent event, Object... params) {
		if (mDownloadTask != null && mDownloadTask.getTask() == task) {
			if (event == TaskEvent.Before) {
			} else if (event == TaskEvent.Cancel) {
				try {
					TDownloadManager.getInstance().onCancelDownloadTask(this);
					TDownloadManager.getInstance().delDownloadTask(getId());
				} catch (Exception e) {
				}
			} else if (event == TaskEvent.Update) {
			} else if (event == TaskEvent.Work) {
				try {
					downloadFile(task);
				} catch (Exception e) {
					mException = e;
					setErrorCode(TDownloadManager.ERROR_UNKONW);
				}
				if (mAndroidHttpClient != null) {
					mAndroidHttpClient.close();
				}

				try {
					if (mRandomAccessFile != null) {
						mRandomAccessFile.close();
						mRandomAccessFile = null;
					}
				} catch (Exception e) {
				}
			}
		}
	}

	private long downloadFile(Task task) throws Exception {
		mAndroidHttpClient = AndroidHttpClient.newInstance(TAG);
		HttpGet httpGet = new HttpGet(mUrl);
		HttpResponse response = mAndroidHttpClient.execute(httpGet);
		mTotalSize = response.getEntity().getContentLength();

		File file = new File(mFilePath, mFileName);

		if (file.length() > 0 && mTotalSize > 0 && mTotalSize > file.length()) {
			httpGet.addHeader("Range", "bytes=" + file.length() + "-");
			mPreviousFileSize = file.length(); // 开始文件的大小

			mAndroidHttpClient.close();
			mAndroidHttpClient = AndroidHttpClient.newInstance("DownloadTask");
			response = mAndroidHttpClient.execute(httpGet);
			TLog.v(TAG, "File is not complete, .");
			TLog.v(TAG, "download now,File length:" + file.length()
					+ " totalSize:" + mTotalSize);
		} else if (file.exists() && mTotalSize == file.length()) {
			TLog.v(TAG, "Output file already exists. Skipping download.");
			mPreviousFileSize = file.length(); // 开始文件的大小
			return 0l;
		}

		long storage = TStorageUtils.getAvailableExternalStorage();
		TLog.i(TAG, "storage:" + storage + " totalSize:" + mTotalSize);
		if (mTotalSize - file.length() > storage) {
			setErrorCode(TDownloadManager.ERROR_SD_NO_MEMORY);
			task.stopTask();
			mAndroidHttpClient.close();
			return 0l;
		}
		try {
			mRandomAccessFile = new ProgressReportingRandomAccessFile(file,
					"rw");
		} catch (FileNotFoundException e) {
			TLog.v(TAG, "OutputStream Error");
		}

		// publishProgress(0, (int) totalSize);
		InputStream input = null;
		try {
			input = response.getEntity().getContent();
		} catch (IOException ex) {
			setErrorCode(TDownloadManager.ERROR_UNKONW);
			mAndroidHttpClient.close();
			TLog.v(TAG, "InputStream Error" + ex.getMessage());
			return 0;
		}

		int bytesCopied = copy(input, mRandomAccessFile, task);

		if ((mPreviousFileSize + bytesCopied) != mTotalSize && mTotalSize != -1
				&& !task.isCancel()) {
			throw new IOException("Download incomplete: " + bytesCopied
					+ " != " + mTotalSize);
		}

		mRandomAccessFile.close();
		mAndroidHttpClient.close();
		mAndroidHttpClient = null;
		mRandomAccessFile = null;
		TLog.v(TAG, "Download completed successfully.");
		return bytesCopied;
	}

	public int copy(InputStream input, RandomAccessFile out, Task task)
			throws Exception, IOException {
		mBuffer = new byte[TDownloadManager.BUFFER_SIZE];
		BufferedInputStream in = new BufferedInputStream(input,
				TDownloadManager.BUFFER_SIZE);
		TLog.v(TAG, "length" + out.length());
		out.seek(out.length());

		int count = 0, byteCount = 0;
		long errorBlockTimePreviousTime = -1, expireTime = 0;
		try {
			while (!task.isCancel()) {
				byteCount = in.read(mBuffer, 0, TDownloadManager.BUFFER_SIZE);
				if (byteCount == -1) {
					break;
				}

				out.write(mBuffer, 0, byteCount);
				count += byteCount;

				if (!TNetWorkUtil.isMobileConnected()) {
					task.stopTask();
					setErrorCode(TDownloadManager.ERROR_BLOCK_INTERNET);
					break;
				}

				if (mSpeed == 0) {
					if (errorBlockTimePreviousTime > 0) {
						expireTime = System.currentTimeMillis()
								- errorBlockTimePreviousTime;
						if (expireTime > TDownloadManager.TIME_OUT) {
							setErrorCode(TDownloadManager.ERROR_BLOCK_INTERNET);
							task.stopTask();
						}
					} else {
						errorBlockTimePreviousTime = System.currentTimeMillis();
					}
				} else {
					expireTime = 0;
					errorBlockTimePreviousTime = -1;
				}
			}
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				setErrorCode(TDownloadManager.ERROR_UNKONW);
				TLog.e(TAG, e.getMessage());
			}

			try {
				in.close();
			} catch (IOException e) {
				setErrorCode(TDownloadManager.ERROR_UNKONW);
				TLog.e(TAG, e.getMessage());
			}
		}

		mBuffer = null;
		return count;
	}

	private final class ProgressReportingRandomAccessFile extends
			RandomAccessFile {
		private int progress = 0;

		public ProgressReportingRandomAccessFile(File file, String mode)
				throws FileNotFoundException {
			super(file, mode);
		}

		@Override
		public void write(byte[] buffer, int offset, int count)
				throws IOException {
			mTotalTime = System.currentTimeMillis() - mStartTime;
			super.write(buffer, offset, count);
			progress += count;
			mDownloadSize = progress;
			mSpeed = mDownloadSize / mTotalTime;
		}
	}
}
