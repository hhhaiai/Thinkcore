package com.thinkcore.exception;

/**
 * @所有异常的基类
 */
public class TException extends Exception {
	private static final long serialVersionUID = 1L;

	public TException() {
		super();
	}

	public TException(String detailMessage) {
		super(detailMessage);
	}
}