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
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

import com.wooki.WookiSymbolsConstants;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.services.WookiModule;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Provide the ability to changes user account settings , like password, name,
 * username and so on.
 */
public class AccountSettings {

	@Inject
	private WookiSecurityContext securityCtx;

	@Inject
	private UserManager userManager;

	@Inject
	private Messages messages;

	@Inject
	@Symbol(value = WookiSymbolsConstants.WOOKI_SALT)
	private String salt;
	
	@Component(id = "userDetails")
	private Form userDetails;

	@Component(id = "username")
	private TextField username;
	
	@Component(id = "passwordChange")
	private Form passwordChange;

	@Property
	private User user;

	@Property
	private String oldPassword;

	@Property
	private String newPassword;

	@Property
	private String newPasswordConfirmation;
	
	@Property
	@Persist("flash")
	private Boolean passwordChangeSuccess;

	/**
	 * Set current user if someone has logged in.
	 * 
	 * @return
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupUser() {
		if (securityCtx.isLoggedIn()) {
			this.user = securityCtx.getAuthor();
			return true;
		}
		return Signin.class;
	}

	@SetupRender
	public Object setupUserSettings() {
		if (this.user == null) {
			return false;
		}
		return true;
	}

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "userDetails")
	void validateUserDetailsChange() {
		User userByUsername = userManager.findByUsername(user.getUsername());

		// check if the new username is not already taken by someone else
		if (userByUsername != null && userByUsername.getId() != user.getId()) {
			userDetails.recordError(username, messages.get("error-username-already-taken"));
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "userDetails")
	void successUserDetailsChange() {
		try {
			this.user = userManager.updateDetails(user);
		} catch (AuthorizationException e) {
			userDetails.recordError(messages.get("error-authorization-exception"));
		} catch (UserAlreadyException e) {
			userDetails.recordError(username, messages.get("error-username-already-taken"));
		}
	}

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "passwordChange")
	void validatePasswordChange() {
		// first, let's check if old password is ok
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String encodedPassword = encoder.encodePassword(oldPassword, this.salt);
		if (!encodedPassword.equals(this.securityCtx.getAuthor().getPassword())) {
			passwordChange.recordError(messages.get("error-old-password-wrong"));	
		}
		
		
		// then check if password confirmation is ok
		if (!newPassword.equals(newPasswordConfirmation)) {
			passwordChange.recordError(messages.get("error-passwords-dont-match"));
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "passwordChange")
	void successPasswordChange() {
		try {
			this.user = userManager.updatePassword(user, oldPassword, newPassword);
			this.passwordChangeSuccess = true;
		} catch (AuthorizationException e) {
			userDetails.recordError(messages.get("error-authorization-exception"));
		}
	}
}
