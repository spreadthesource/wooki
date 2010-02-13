package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

public class IgnoredContentTypesFilter implements RequestFilter {

	private ComponentEventLinkEncoder linkEncoder;

	public IgnoredContentTypesFilter(ComponentEventLinkEncoder linkEncoder) {

		this.linkEncoder = linkEncoder;
	}

	public boolean service(Request request, Response response, RequestHandler requesthandler) throws IOException {

		// String path = request.getPath();
		//
		// if (path.startsWith(RequestConstants.ASSET_PATH_PREFIX))
		// return true;
		//
		// ComponentEventRequestParameters parameters =
		// linkEncoder.decodeComponentEventRequest(request);
		// if (parameters == null) {
		// System.out.println(request.getHeader("content-type"));
		// }
		// return false;
		return true;
	}
}
