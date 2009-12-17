package com.wooki.base;

import java.security.Principal;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

public class WookiBase {

	@Inject
	private RequestGlobals requestGlobals;

	@Property
	private String username;

	private boolean logged;

	@SetupRender
	private void setupUsername() {
		Principal principal = requestGlobals.getHTTPServletRequest()
				.getUserPrincipal();
		this.logged = principal != null && principal.getName() != "";
		this.username = principal != null ? principal.getName() : null;
	}

	public boolean isLogged() {
		return logged;
	}

}
