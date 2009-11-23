package com.wooki.pages.book;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.ioc.annotations.Inject;

public class IndexDesign {
	@Inject
	private RenderSupport support;
	
	@AfterRender
	void addScript() {
		support.addScript("Wooki.bubbles.init();");
	}
}
