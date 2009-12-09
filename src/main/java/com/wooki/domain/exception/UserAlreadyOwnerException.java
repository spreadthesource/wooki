package com.wooki.domain.exception;

public class UserAlreadyOwnerException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1424814279486457742L;

	public UserAlreadyOwnerException() {
		super();
	}

	public UserAlreadyOwnerException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyOwnerException(String message) {
		super(message);
	}

	public UserAlreadyOwnerException(Throwable cause) {
		super(cause);
	}

}
