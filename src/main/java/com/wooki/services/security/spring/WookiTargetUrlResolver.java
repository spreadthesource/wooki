//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services.security.spring;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.Authentication;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.savedrequest.SavedRequest;

import com.wooki.services.WookiModule;

/**
 * Implement a custom target url resolver for spring security based on cookies.
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
