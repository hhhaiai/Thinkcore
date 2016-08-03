package com.thinkcore.exception;

/**
 * 是当没有找到相应名字布局，抛出此异常！
 */
public class TNoSuchNameLayoutException extends TException {
	private static final long serialVersionUID = 2780151262388197741L;

	public TNoSuchNameLayoutException() {
		super();
	}

	public TNoSuchNameLayoutException(String detailMessage) {
		super(detailMessage);
	}
}
