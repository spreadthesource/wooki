package com.wooki.domain.exception;

public class UserNotFoundException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1424814279486457742L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

}
