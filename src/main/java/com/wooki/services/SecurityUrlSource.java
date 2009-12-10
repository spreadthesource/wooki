package com.wooki.services;

/**
 * Wrapper around spring security bean to obtain the URL with the context.
 *
 * @author ccordenier
 *
 */
public interface SecurityUrlSource {

	/**
	 * Obtain the login URL.
	 *
	 * @return
	 */
	String getLoginUrl();
	
	/**
	 * Return the logout URL.
	 *
	 * @return
	 */
	String getLogoutUrl();
}
