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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Signup page for new authors.
 */
public class Signup {

	@InjectComponent
	private Form signupForm;

	@InjectComponent(value = "username")
	private Field usernameF;

	@InjectComponent(value = "password")
	private Field passwordF;

	@Inject
	private UserManager userManager;

	@Inject
	private WookiSecurityContext securityCtx;

	@InjectPage
	private Dashboard successPage;

	@Property
	@Validate("required,maxLength=20")
	private String username;

	@Property
	@Validate("required")
	private String fullname;

	@Property
	@Validate("required,minLength=8")
	private String password;

	@Property
	@Validate("required,email")
	private String email;

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "signupForm")
	public void onValidate() {
		if (username != null && password != null && username.trim().compareToIgnoreCase(password.trim()) == 0) {
			signupForm.recordError(passwordF, "User password cannot be its username");
		}
		// Do a first check
		if (username != null && userManager.findByUsername(username) != null) {
			signupForm.recordError(usernameF, "User already exists");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "signupForm")
	public Object onSignupSuccess() {

		try {
			User user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);
			user.setFullname(this.fullname);
			userManager.addUser(user);

			securityCtx.log(user);

			successPage.setFirstAccess(true);
			return successPage;

		} catch (UserAlreadyException uaeEx) {
			signupForm.recordError("User already exists");
			return this;
		}
	}

}
