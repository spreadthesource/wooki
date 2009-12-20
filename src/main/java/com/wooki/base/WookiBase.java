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
