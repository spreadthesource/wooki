package com.wooki.domain.exception;

public class TitleAlreadyInUseException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1424814279486457742L;

	public TitleAlreadyInUseException() {
		super();
	}

	public TitleAlreadyInUseException(String message, Throwable cause) {
		super(message, cause);
	}

	public TitleAlreadyInUseException(String message) {
		super(message);
	}

	public TitleAlreadyInUseException(Throwable cause) {
		super(cause);
	}

}
