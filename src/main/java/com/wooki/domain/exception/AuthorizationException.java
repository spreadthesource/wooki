package com.wooki.domain.exception;

/**
 * Used to raise authentication related exception.
 * 
 * @author ccordenier
 * 
 */
public class AuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5440438345681985807L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

}
