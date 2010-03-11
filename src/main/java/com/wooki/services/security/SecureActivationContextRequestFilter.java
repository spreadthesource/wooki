package com.wooki.services.security;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.upload.services.MultipartDecoder;

/**
 * Verify that the activation context passed corresponds to an existing
 * activation method. Send 404 error if context method is not available.
 * 
 * @author ccordenier
 * 
 */
public class SecureActivationContextRequestFilter implements ComponentRequestFilter {

	private final ActivationContextManager manager;

	private final Response response;

	private final MultipartDecoder decoder;
	
	public SecureActivationContextRequestFilter(ActivationContextManager manager, Response response, MultipartDecoder decoder) {
		this.manager = manager;
		this.response = response;
		this.decoder = decoder;
	}

	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler) throws IOException {

		// Do not check in case of upload exception
		if(decoder.getUploadException() != null) {
			handler.handleComponentEvent(parameters);
			return;
		}
		
		if (manager.checkContext(parameters.getContainingPageName(), parameters.getPageActivationContext())) {
			handler.handleComponentEvent(parameters);
			return;
		}
		this.response.sendError(404, "Resource not found");

	}

	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException {

		// Do not check in case of upload exception
		if(decoder.getUploadException() != null) {
			handler.handlePageRender(parameters);
			return;
		}

		if (manager.checkContext(parameters.getLogicalPageName(), parameters.getActivationContext())) {
			handler.handlePageRender(parameters);
			return;
		}
		this.response.sendError(404, "Resource not found");
	}

}
