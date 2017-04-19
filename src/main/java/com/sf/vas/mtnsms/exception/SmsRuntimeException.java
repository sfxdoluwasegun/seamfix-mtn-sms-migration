/**
 * 
 */
package com.sf.vas.mtnsms.exception;

/**
 * @author dawuzi
 *
 */
public class SmsRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -3513445142575759856L;

	public SmsRuntimeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SmsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	/**
	 * @param message
	 * @param cause
	 */
	public SmsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * @param message
	 */
	public SmsRuntimeException(String message) {
		super(message);
	}
	/**
	 * @param cause
	 */
	public SmsRuntimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
