package com.wooki.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;
import org.apache.tapestry5.services.Request;

import com.wooki.services.security.WookiSecurityContext;

public class SecurityUrlSourceImpl implements SecurityUrlSource {

	@Inject
	private Request request;
	
	private final static Messages MESSAGES = MessagesImpl.forClass(WookiSecurityContext.class);
	
	public String getLoginUrl() {
		return request.getContextPath() + MESSAGES.get("loginFilterProcessesUrl");
	}

	public String getLogoutUrl() {
		// TODO Auto-generated method stub
		return request.getContextPath() + MESSAGES.get("logoutFilterProcessUrl");
	}

}
