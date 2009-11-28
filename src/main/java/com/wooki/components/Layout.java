package com.wooki.components;

import nu.localhost.tapestry5.springsecurity.services.LogoutService;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;

import com.wooki.base.WookiBase;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private LogoutService logoutService;

	@Inject
	private RenderSupport support;
	
	@Inject
	@Value("${spring-security.check.url}")
	private String checkUrl;

	@Inject
	private Request request;

	/**
	 * Simply invalidate the session and return to signin page.
	 * 
	 * @return
	 */
	@OnEvent(component = "logout")
	public void logout() {
		logoutService.logout();
	}

	@AfterRender
	public void initLoginDialog() {
		if (!isLogged()) {
			support.addInit("initLoginDialog");
		}
	}
	
	public String getLoginCheckUrl() {
		return request.getContextPath() + checkUrl;
	}

}
