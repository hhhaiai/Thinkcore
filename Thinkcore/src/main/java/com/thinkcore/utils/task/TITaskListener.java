package com.thinkcore.utils.task;

import com.thinkcore.utils.task.TTask.Task;
import com.thinkcore.utils.task.TTask.TaskEvent;

public interface TITaskListener {
	/*
	 * Update ->int Cancel ->boolean
	 */
	public void onTask(Task task, TaskEvent event, Object... params);
}
