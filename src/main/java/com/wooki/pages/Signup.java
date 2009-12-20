package com.wooki.pages;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.services.WookiModule;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Signup page for new authors.
 */
public class Signup {

	@InjectComponent
	private Form signupForm;

	@Inject
	private UserManager userManager;

	@Inject
	private WookiSecurityContext securityCtx;

	@Inject
	private Cookies cookies;

	@InjectPage
	private Index successPage;

	@Property
	@Validate("required")
	private String username;

	@Property
	@Validate("required")
	private String fullname;

	@Property
	@Validate("required")
	private String password;

	@Property
	@Validate("required,email")
	private String email;

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "signupForm")
	public void onValidate() {
		// Do a first check
		if (userManager.findByUsername(username) != null) {
			signupForm.recordError("User already exists");
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

			String referer = cookies.readCookieValue(WookiModule.VIEW_REFERER);
			if (referer != null) {
				try {
					return new URL(referer);
				} catch (MalformedURLException e) {
					return successPage;
				}
			}

			return successPage;
			
		} catch (UserAlreadyException uaeEx) {
			signupForm.recordError("User already exists");
			return this;
		}
	}

}
