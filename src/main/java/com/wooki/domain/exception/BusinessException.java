package com.wooki.domain.exception;

/**
 * Base exception for service layer exceptions.
 * 
 * @author ccordenier
 * 
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -9148405210491866810L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
