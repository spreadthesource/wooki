package com.wooki.services.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.Authentication;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.savedrequest.SavedRequest;

import com.wooki.services.WookiModule;

/**
 * Implement a custom target url resolver for spring security.
 *
 * @author ccordenier
 *
 */
public class WookiTargetUrlResolver implements TargetUrlResolver {

	private final TargetUrlResolver defaultTargetUrlResolver;
	
	public WookiTargetUrlResolver(TargetUrlResolver defaultTargetUrlResolver) {
		this.defaultTargetUrlResolver = defaultTargetUrlResolver;
	}

	public String determineTargetUrl(SavedRequest savedRequest,
			HttpServletRequest currentRequest, Authentication auth) {
		// Check first wooki last view
		Cookie [] cookies = currentRequest.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(WookiModule.VIEW_REFERER.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return defaultTargetUrlResolver.determineTargetUrl(savedRequest, currentRequest, auth);
	}

}
