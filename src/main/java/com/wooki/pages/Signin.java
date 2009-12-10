package com.wooki.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.SecurityUrlSource;

/**
 * Login form.
 * 
 * @author ccordenier
 * 
 */
public class Signin {

	@Inject
	private SecurityUrlSource source;

	@Property
	private String loginUrl;

	private boolean failed = false;

	@SetupRender
	public void setup() {
		loginUrl = source.getLoginUrl();
	}

	public boolean isFailed() {
		return failed;
	}

	void onActivate(String extra) {
		if (extra.equals("failed")) {
			failed = true;
		}
	}
}
