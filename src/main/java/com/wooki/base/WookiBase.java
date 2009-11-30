package com.wooki.base;

import java.security.Principal;

import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.security.context.SecurityContextHolder;

public class WookiBase {

	@Inject
	private RequestGlobals requestGlobals;

	@Property
	private String username;

	private boolean logged;

	@PageAttached
	private void setupUsername() {
		username = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		Principal principal = requestGlobals.getHTTPServletRequest()
				.getUserPrincipal();
		this.logged = principal != null && principal.getName() != "";
	}

	public boolean isLogged() {
		return logged;
	}

}
