package com.treecore.utils.task;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.treecore.utils.TAndroidVersionUtils;

import android.os.AsyncTask;
import android.util.Log;

public class TTask {
	private final static String TAG = TTask.class.getCanonicalName();

	public enum TaskEvent {
		Before, Update, Cancel, Work
	}

	protected Task mTask;
	protected TITaskListener mIListener;
	// protected TTaskHandler mHandler;
	private static ExecutorService mExecutorService;

	private static int Update = 1;

	static {
		// SINGLE_TASK_EXECUTOR = (ExecutorService) Executors
		// .newSingleThreadExecutor();
		mExecutorService = (ExecutorService) Executors.newFixedThreadPool(30);// 30个线程
		// mExecutorService = (ExecutorService) Executors.newCachedThreadPool();
	};

	public boolean equalTask(Task task) {
		return mTask == task;
	}

	public Task getTask() {
		return mTask;
	}

	public void newTask1(int TaskId, String... params) {
		stopTask();
		mTask = new Task(TaskId, params);
	}

	public void executeTask1(String... params) {
		if (TAndroidVersionUtils.hasHoneycomb()) {
			mTask.executeOnExecutor(mExecutorService, "");
		} else {
			mTask.execute("");
		}
	}

	public void startTask(int TaskId) {
		stopTask();

		mTask = null;
		mTask = new Task(TaskId);
		if (TAndroidVersionUtils.hasHoneycomb()) {
			mTask.executeOnExecutor(mExecutorService, "");
		} else {
			mTask.execute("");
		}
	}

	public void startTask(int TaskId, String... params) {
		stopTask();

		mTask = new Task(TaskId, params);
		if (TAndroidVersionUtils.hasHoneycomb()) {
			mTask.executeOnExecutor(mExecutorService, params);
		} else {
			mTask.execute(params);
		}
	}

	public void startTask(String... params) {
		stopTask();

		mTask = new Task(0, params);
		if (TAndroidVersionUtils.hasHoneycomb()) {
			mTask.executeOnExecutor(mExecutorService, params);
		} else {
			mTask.execute(params);
		}
	}

	public void stopTask() {
		if (mTask != null) {
			mTask.stopTask();
		}
	}

	public boolean isTasking() {
		if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
			return true;
		}
		return false;
	}

	public void setIXTaskListener(TITaskListener listener) {
		mIListener = listener;
	}

	public class Task extends AsyncTask<String, Integer, Boolean> {
		protected String mErrorString = "";
		protected Task mThis;
		private int mTaskId = 0;
		private boolean mBCancel = false;
		private ArrayList<String> mParameters = new ArrayList<String>();
		private Object mResultObject;

		public Task(int taskId, String... params) {
			mTaskId = taskId;
			mThis = this;
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					mParameters.add(params[i]);
				}
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;
			if (mBCancel)
				return false;
			if (mIListener != null) {
				mIListener.onTask(this, TaskEvent.Work, params);
			}
			return true;
		}

		@Override
		protected void onPreExecute() {
			if (mBCancel)
				return;
			super.onPreExecute();
			mResultObject = null;
			if (mIListener != null) {
				mIListener.onTask(this, TaskEvent.Before);
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mBCancel)
				return;
			super.onPostExecute(result);
			if (mIListener != null) {
				mIListener.onTask(this, TaskEvent.Cancel, result);
			}
			if (mErrorString != null && !mErrorString.equals(""))
				Log.i(TAG, this.getTaskId() + mErrorString);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mBCancel = true;
			if (mIListener != null) {
				mIListener.onTask(this, TaskEvent.Cancel, false);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (mBCancel)
				return;

			super.onProgressUpdate(values);

			if (mIListener != null) {
				mIListener.onTask(this, TaskEvent.Update, values);
			}
		}

		public void publishTProgress(int values) {
			mTask.publishProgress(values);
		}

		public ArrayList<String> getParameter() {
			return mParameters;
		}

		public void stopTask() {
			mParameters.clear();
			mBCancel = true;
			cancel(true);
		}

		public void setResultObject(Object object) {
			mResultObject = object;
		}

		public Object getResultObject() {
			return mResultObject;
		}

		public void setError(String error) {
			mErrorString = error;
		}

		public String getError() {
			return mErrorString;
		}

		public void setTaskId(int taskId) {
			mTaskId = taskId;
		}

		public int getTaskId() {
			return mTaskId;
		}

		public boolean isCancel() {
			return mBCancel;
		}
	}

	// private class TTaskHandler extends THandler<Task> {
	// private TIHandler mIHandler;
	//
	// public TTaskHandler(Task owner) {
	// super(owner);
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	// if (mIHandler == null || getOwner() == null)
	// return;
	//
	// mIHandler.handleMessage(msg);
	// }
	//
	// public void setIHandler(TIHandler handler) {
	// mIHandler = handler;
	// }
	// };

}
