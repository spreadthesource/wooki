package com.wooki.base;

import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.Property;
import org.springframework.security.context.SecurityContextHolder;

public class WookiBase {

	@Property
	private String username;

	@PageAttached
	private void setupUsername() {
		username = SecurityContextHolder.getContext().getAuthentication()
				.getName();
	}

}
