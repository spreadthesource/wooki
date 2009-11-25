package com.wooki.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.wooki.domain.model.User;
import com.wooki.services.UserManager;

/**
 * Signup page for new authors.
 * 
 * @author ccordenier
 * 
 */
public class Signup {

	@InjectComponent
	private Form signupForm;

	@Inject
	private ApplicationContext context;

	private UserManager userManager;

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
	@Validate("required")
	private String confirmPassword;

	@Property
	@Validate("required,email")
	private String email;

	@OnEvent(value = EventConstants.PREPARE, component = "signupForm")
	public void prepareSubmit() {
		userManager = (UserManager) context.getBean("userManager");
	}

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "signupForm")
	public void onValidate() {
		if (!password.equals(confirmPassword)) {
			signupForm.recordError("Password do not match");
		}
		if (userManager.findByUsername(username) != null) {
			signupForm.recordError("User already exists");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "signupForm")
	public Object onSignupSuccess() {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setFullname(this.fullname);
		userManager.addUser(user);
		
		// Alert spring security that an author has logged in
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user, user
						.getAuthorities()));
		successPage.setUsername(username);
		return successPage;
	}

}
