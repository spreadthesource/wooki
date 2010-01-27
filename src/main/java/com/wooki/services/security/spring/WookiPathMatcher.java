package com.wooki.services.security.spring;

/**
 * Define a simple matching interface.
 *
 * @author ccordenier
 *
 */
public interface WookiPathMatcher {

	boolean matches(String url);
	
}
