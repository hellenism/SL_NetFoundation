package com.sl.net.exception;

public class NetException extends Exception{
	
	// ------ members ------
	
	/**
	 * “Ï≥£±‡∫≈
	 */
	private int mExceptionCode;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ------ getter , setter ------
	public int getmExceptionCode() {
		return mExceptionCode;
	}

	public void setmExceptionCode(int mExceptionCode) {
		this.mExceptionCode = mExceptionCode;
	}

	// ------ constructions ------
	public NetException() {
		super();
	}

	public NetException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetException(String detailMessage) {
		super(detailMessage);
	}

	public NetException(Throwable throwable) {
		super(throwable);
	}
	
	public NetException(int exceptionCode)
	{
		this();
		mExceptionCode = exceptionCode;
	}
	
	// ------ 
	
}
