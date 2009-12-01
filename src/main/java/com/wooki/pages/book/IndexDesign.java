package com.wooki.pages.book;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

public class IndexDesign {
	@Inject
	private RenderSupport support;
	
	@AfterRender
	void addScript() {
		JSONObject data = new JSONObject();
		data.put("b1", "3");
		data.put("b2", "5");
		support.addInit("initBubbles", data);
	}
}
