package com.wooki.services;

/**
 * Wrapper around spring security bean to obtain the URL with the context.
 *
 * @author ccordenier
 *
 */
public interface LoginUrlSource {

	/**
	 * Obtain the login URL.
	 *
	 * @return
	 */
	String getLoginUrl();
}
