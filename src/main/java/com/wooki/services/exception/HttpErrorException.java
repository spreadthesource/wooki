package com.wooki.services.exception;

import com.wooki.services.HttpError;

public class HttpErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2777810238630399039L;

	private HttpError httpError;

	public HttpErrorException(HttpError httpError) {
		super();
		this.httpError = httpError;
	}

	public HttpErrorException(HttpError httpError, String message, Throwable cause) {
		super(message, cause);
		this.httpError = httpError;
	}

	public HttpErrorException(HttpError httpError, String message) {
		super(message);
		this.httpError = httpError;
	}

	public HttpErrorException(HttpError httpError, Throwable cause) {
		super(cause);
		this.httpError = httpError;
	}

	public HttpError getHttpError() {
		return httpError;
	}

	public void setHttpError(HttpError httpError) {
		this.httpError = httpError;
	}

}
