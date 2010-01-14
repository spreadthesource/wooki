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

package com.wooki.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;

public class SecurityUrlSourceImpl implements SecurityUrlSource {

	@Inject
	private Request request;

	@Inject
	private Cookies cookieSource;

	@Inject
	@Symbol("loginFilterProcessesUrl")
	private String loginUrl;

	@Inject
	@Symbol("logoutFilterProcessUrl")
	private String logoutUrl;

	public String getLoginUrl() {
		return request.getContextPath() + loginUrl;
	}

	public String getLogoutUrl() {
		String result = request.getContextPath() + logoutUrl;
		String viewReferer = cookieSource.readCookieValue(WookiModule.VIEW_REFERER);
		if (viewReferer != null) {
			result += "?logoutSuccessUrl=" + viewReferer;
		}
		return result;
	}

}
