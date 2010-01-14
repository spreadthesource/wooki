package com.wooki.services.security.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * This filter send a 404 HTTP error if the requested URL does not correspond to
 * a public tapestry URL.
 * 
 * @author ccordenier
 * 
 */
public class TapestryPublicResourceFilter extends TapestrySpringSecurityFilter {

	/**
	 * This is the list of pages that are declared as public Tapestry pages. If
	 * the request does not page a page in this list, then the user receive a
	 * 404 HTTP error.
	 * 
	 */
	private List<String> publicPages = new ArrayList<String>();

	/**
	 * All the public pages are
	 * 
	 * @param publicPages
	 */
	public TapestryPublicResourceFilter(List<String> publicPages) {
		if (publicPages != null) {
			for (String pageName : publicPages) {
				this.publicPages.add(pageName.toLowerCase());
			}
		}
	}

	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	@Override
	protected void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Secure Render request
		PageRenderRequestParameters params = this.decodePageRenderRequest(request);
		if (params != null) {
			String logicalPageName = params.getLogicalPageName();
			if (!this.publicPages.contains(logicalPageName.toLowerCase())) {
				throw new IllegalArgumentException(
						"Your security configuration is maybe missing something, check to verify that the page you are trying to access is well configured");
			}
		}

		// Secure actions request
		ComponentEventRequestParameters actionParams = this.decodeComponentEventRequest(request);
		if (actionParams != null) {
			String logicalPageName = actionParams.getContainingPageName();
			if (!this.publicPages.contains(logicalPageName.toLowerCase())) {
				throw new IllegalArgumentException(
						"Your security configuration is maybe missing something, check to verify that the page you are trying to access is well configured");
			}
		}

		chain.doFilter(request, response);
	}

}
