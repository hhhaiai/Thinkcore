package com.thinkcore.utils.task;

import java.util.LinkedList;
import java.util.Queue;

public class TQueueTask {
	private Queue<TTask> mTTasks = new LinkedList<TTask>();

	public Queue<TTask> getTasks() {
		return mTTasks;
	}

	public boolean addTask(TTask task) {
		TTask innerTask = hasTask(task.getTask().getTaskId());
		return innerTask != null ? false : mTTasks.add(task);
	}

	public boolean delTask(int taskId) {
		TTask task = hasTask(taskId);
		return task == null ? false : mTTasks.remove(task);
	}

	public boolean delTask(TTask task) {
		return mTTasks.remove(task);
	}

	public TTask hasTask(int taskId) {
		for (TTask task : mTTasks) {
			if (task.getTask().getTaskId() == taskId)
				return task;
		}
		return null;
	}

	public void clearTask() {
		for (TTask task : mTTasks) {
			task.stopTask();
			task = null;
		}

		mTTasks.clear();
	}
}
