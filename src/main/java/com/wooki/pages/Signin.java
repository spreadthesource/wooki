package com.wooki.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;

/**
 * Login form.
 * 
 * @author ccordenier
 * 
 */
public class Signin {

	@Inject
	@Value("${spring-security.check.url}")
	private String checkUrl;

	@Inject
	private Request request;

	private boolean failed = false;

	public boolean isFailed() {
		return failed;
	}

	public String getLoginCheckUrl() {
		return request.getContextPath() + checkUrl;
	}

	void onActivate(String extra) {
		if (extra.equals("failed")) {
			failed = true;
		}
	}
}
