package com.wooki.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;

import com.wooki.pages.book.Index;
import com.wooki.services.AuthorManager;

/**
 * Login form.
 * 
 * @author ccordenier
 * 
 */
public class Signin {

	@InjectComponent
	private Form loginForm;

	@Inject
	private ApplicationContext context;

	private AuthorManager authorManager;

	@Property
	@Validate("required")
	private String username;

	@Property
	@Validate("required")
	private String password;

	@Property
	private boolean rememberMe;
	
	@OnEvent(value = EventConstants.PREPARE_FOR_SUBMIT, component = "loginForm")
	public void prepareSubmit() {
		authorManager = (AuthorManager) context.getBean("authorManager");
	}

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "loginForm")
	public void onValidate() {
		if (!authorManager.checkPassword(username, password)) {
			loginForm.recordError("Wrong username or password");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "loginForm")
	public Object onLoginSuccess() {
		return Index.class;
	}

}
