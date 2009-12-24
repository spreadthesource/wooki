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
