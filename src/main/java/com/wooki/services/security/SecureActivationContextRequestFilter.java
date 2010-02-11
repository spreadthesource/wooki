package com.wooki.services.security;

import java.io.IOException;

import org.apache.tapestry5.internal.services.PageRenderDispatcher;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

/**
 * Verify that the activation context passed corresponds to an existing
 * activation method. Send 404 error if context method is not available.
 * 
 * @author ccordenier
 * 
 */
public class SecureActivationContextRequestFilter extends PageRenderDispatcher {

	private final ActivationContextManager manager;



	private final ComponentEventLinkEncoder linkEncoder;

	public SecureActivationContextRequestFilter(ActivationContextManager manager, ComponentRequestHandler componentRequestHandler,
			ComponentEventLinkEncoder linkEncoder) {
		super(componentRequestHandler, linkEncoder);
		this.linkEncoder = linkEncoder;
		this.manager = manager;
	}

	@Override
	public boolean dispatch(Request request, Response response) throws IOException {
		PageRenderRequestParameters parameters = linkEncoder.decodePageRenderRequest(request);

		if (manager.checkContext(parameters.getLogicalPageName(), parameters.getActivationContext()))
			return super.dispatch(request, response);

		return false;
	}
}
