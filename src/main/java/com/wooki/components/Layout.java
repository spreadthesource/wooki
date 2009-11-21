package com.wooki.components;

import nu.localhost.tapestry5.springsecurity.services.LogoutService;

import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.WookiBase;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private LogoutService logoutService;

	/**
	 * Simply invalidate the session and return to signin page.
	 * 
	 * @return
	 */
	@OnEvent(component = "logout")
	public void logout() {
		logoutService.logout();
	}

}
