package com.wooki.services.security.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * This matcher matched all the public Tapestry URL resources.
 * 
 * @author ccordenier
 * 
 */
public class TapestryPublicUrlPathMatcher extends AbstractTapestryUrlPathMatcher {

	/**
	 * List of public pages.
	 */
	private List<String> publicPages = new ArrayList<String>();

	/**
	 * All the public pages are
	 * 
	 * @param publicPages
	 */
	public TapestryPublicUrlPathMatcher(List<String> publicPages) {
		if (publicPages != null) {
			for (String pageName : publicPages) {
				this.publicPages.add(pageName.toLowerCase());
			}
		}
	}

	/**
	 * This method tries to find a declared public in tapestry.
	 * 
	 */
	public boolean matches(String url) {

		// Secure Render request
		PageRenderRequestParameters params = this.decodePageRenderRequest(url);
		if (params != null) {
			String logicalPageName = params.getLogicalPageName();
			if (this.publicPages.contains(logicalPageName.toLowerCase())) {
				return true;
			}
		}

		// Secure actions request
		ComponentEventRequestParameters actionParams = this.decodeComponentEventRequest(url);
		if (actionParams != null) {
			String logicalPageName = actionParams.getContainingPageName();
			if (this.publicPages.contains(logicalPageName.toLowerCase())) {
				return true;
			}
		}

		return false;

	}

}
