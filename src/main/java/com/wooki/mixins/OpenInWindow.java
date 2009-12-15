package com.wooki.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * Open the link in a new window.
 *
 * @author ccordenier
 *
 */
@MixinAfter
public class OpenInWindow {

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value="componentResources.id")
	private String name;

	@Parameter(defaultPrefix= BindingConstants.LITERAL, value="directories=no,location=no,menubar=no,resizable=yes,toolbar=no")
	private String options;
	
	@Inject
	private RenderSupport renderSupport;
	
	@InjectContainer
	private AbstractLink link;
	
	@AfterRender
	public void initLink() {
		JSONObject param = new JSONObject();
		param.put("elt", link.getClientId());
		param.put("url", link.getLink().toAbsoluteURI());
		param.put("name", name);
		param.put("options", options);
		renderSupport.addInit("initOpenInWindow", param);
	}
	
}
