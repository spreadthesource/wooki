package com.wooki.services.security;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * Verify if a resource is authorized or not.
 * 
 * @author ccordenier
 * 
 */
public interface TapestryResourceAccessController {

	/**
	 * Check if the user is authorized to access the resource.
	 * 
	 * @param activationContext
	 * @return
	 */
	boolean isViewAuthorized(PageRenderRequestParameters params);

	/**
	 * Check if the user is authorized to the Tapestry action.
	 * 
	 * @param params
	 * @return
	 */
	boolean isActionAuthorized(ComponentEventRequestParameters params);

}
