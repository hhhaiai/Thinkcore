package com.thinkcore.utils.task;

import java.util.Timer;
import java.util.TimerTask;

public class TTimerTask {
	private ITimerListener mListener;
	private long mDelayMs;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private boolean isStarted = false;
	private TTimerTask mThis;

	/**
	 * 构造函数
	 * 
	 * @param delayMs
	 *            延时
	 * @param listen
	 *            定时处理器，由调用者定制实现
	 */
	public TTimerTask(int delayMs, ITimerListener listen) {
		mListener = listen;
		mDelayMs = delayMs;
		mThis = this;
	}

	/**
	 * 启动定时器
	 */
	public void startTimer(boolean bFlag) {
		stopTimer();
		mTimer = new Timer(true);
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				if (null != mListener && isRunning()) {
					mListener.onTimerListen(mThis);
				}
			}

		};
		if (bFlag) {
			mTimer.schedule(mTimerTask, 0, mDelayMs);
		} else {
			mTimer.schedule(mTimerTask, mDelayMs);
		}
		isStarted = true;
	}

	/**
	 * 停止定时器
	 */
	public void stopTimer() {
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
		}
		if (null != mTimerTask) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		isStarted = false;
	}

	public boolean isRunning() {
		return isStarted;
	}

	public interface ITimerListener {
		public abstract void onTimerListen(TTimerTask task);
	}
}
