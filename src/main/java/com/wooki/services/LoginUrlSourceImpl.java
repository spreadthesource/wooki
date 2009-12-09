package com.wooki.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

public class LoginUrlSourceImpl implements LoginUrlSource {

	@Inject
	private Request request;
	
	@Inject
	private AuthenticationProcessingFilter authFilter;
	
	public String getLoginUrl() {
		return request.getContextPath() + authFilter.getDefaultFilterProcessesUrl();
	}

}
