package com.wooki.components;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.WookiBase;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private RenderSupport support;

	@AfterRender
	public void initLoginDialog() {
		if (!isLogged()) {
			support.addInit("initLoginDialog");
		}
	}

}
