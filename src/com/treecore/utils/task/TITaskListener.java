package com.treecore.utils.task;

import com.treecore.utils.task.TTask.Task;
import com.treecore.utils.task.TTask.TaskEvent;

public interface TITaskListener {
	/*
	 * Update ->int Cancel ->boolean
	 */
	public void onTask(Task task, TaskEvent event, Object... params);
}
