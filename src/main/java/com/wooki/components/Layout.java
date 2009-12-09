package com.wooki.components;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.WookiBase;
import com.wooki.services.LoginUrlSource;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private RenderSupport support;

	@Inject
	private LoginUrlSource source;

	@Property
	private String loginUrl;
	
	@SetupRender
	private void setup(){
		this.loginUrl = source.getLoginUrl();
	}
	
	@AfterRender
	public void initLoginDialog() {
		if (!isLogged()) {
			support.addInit("initLoginDialog");
		}
	}

}
