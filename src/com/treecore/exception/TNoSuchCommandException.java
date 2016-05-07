package com.treecore.exception;

/**
 * 是当没有找到相应资源ID名字的资源时，抛出此异常！
 */
public class TNoSuchCommandException extends TException {
	private static final long serialVersionUID = 1L;

	public TNoSuchCommandException() {
		super();
	}

	public TNoSuchCommandException(String detailMessage) {
		super(detailMessage);
	}
}
