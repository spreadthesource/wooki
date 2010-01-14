package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventResultProcessor;

import com.wooki.services.exception.HttpErrorException;

public class HttpErrorResultProcessor implements ComponentEventResultProcessor<HttpError> {

	public void processResultValue(HttpError value) throws IOException {
		if (value != null) {
			throw new HttpErrorException(value);
		}
	}

}