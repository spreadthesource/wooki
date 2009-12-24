package com.wooki.pages;

import javax.servlet.http.Cookie;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.ExceptionReporter;

import com.wooki.services.WookiModule;

/**
 * Default exception page.
 * 
 * @author ccordenier
 * 
 */
public class WookiExceptionReport implements ExceptionReporter {

	@Inject
	private Cookies cookieSource;

	@Property
	private boolean displayLastViewUrl;

	@Property
	private String lastUrl;

	public void reportException(Throwable exception) {

	}

	@SetupRender
	public void setupLastUrl() {
		lastUrl = cookieSource.readCookieValue(WookiModule.VIEW_REFERER);
		if (lastUrl != null) {
			displayLastViewUrl = true;
		}
	}

}
