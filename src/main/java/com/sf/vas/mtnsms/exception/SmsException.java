/**
 * 
 */
package com.sf.vas.mtnsms.exception;

/**
 * @author dawuzi
 *
 */
public class SmsException extends Exception {

	private static final long serialVersionUID = 5829331440407113525L;

	/**
	 * 
	 */
	public SmsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SmsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SmsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SmsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
