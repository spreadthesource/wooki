package com.wooki.services.security;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import com.wooki.services.HttpError;
import com.wooki.services.exception.HttpErrorException;

/**
 * Verify that the activation context passed corresponds to an existing
 * activation method. Send 404 error if context method is not available.
 * 
 * @author ccordenier
 * 
 */
public class SecureActivationContextRequestFilter implements ComponentRequestFilter {

	private final ActivationContextManager manager;

	public SecureActivationContextRequestFilter(ActivationContextManager manager) {
		this.manager = manager;
	}

	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
		if (manager.checkContext(parameters.getContainingPageName(), parameters.getPageActivationContext())) {
			handler.handleComponentEvent(parameters);
			return;
		}
		throw new HttpErrorException(new HttpError(404, "Resource not found"));
	}

	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
		if (manager.checkContext(parameters.getLogicalPageName(), parameters.getActivationContext())) {
			handler.handlePageRender(parameters);
			return;
		}
		throw new HttpErrorException(new HttpError(404, "Resource not found"));
	}

}
