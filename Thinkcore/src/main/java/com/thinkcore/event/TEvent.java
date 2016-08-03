package com.thinkcore.event;

public class TEvent {
	protected int mMainEvent = -1;
	protected int mSubEvent = -1;
	protected Object mParams = null;

	public TEvent() {
	}

	public TEvent(int mainEvent) {
		mMainEvent = mainEvent;
	}

	public TEvent(int mainEvent, int subEvent) {
		mMainEvent = mainEvent;
		mSubEvent = subEvent;
	}

	public TEvent(int mainEvent, int subEvent, Object params) {
		mMainEvent = mainEvent;
		mSubEvent = subEvent;
		mParams = params;
	}

	public TEvent(int mainEvent, Object params) {
		mMainEvent = mainEvent;
		mParams = params;
	}

	public int getMainEvent() {
		return mMainEvent;
	}

	public int getSubEvent() {
		return mMainEvent;
	}

	public Object getParams() {
		return mParams;
	}
}
