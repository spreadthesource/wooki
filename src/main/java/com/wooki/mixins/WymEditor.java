package com.wooki.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * Integrate wymeditor as a mixin to be used with textarea.
 * 
 * @author ccordenier
 * 
 */
@IncludeJavaScriptLibrary("context:/static/js/wymeditor/jquery.wymeditor.pack.js")
public class WymEditor {

	@Parameter(defaultPrefix = BindingConstants.ASSET)
	private String wymStyle;

	@InjectContainer
	private TextArea container;

	@Inject
	private RenderSupport renderSupport;

	@AfterRender
	public void attachWymEditor() {

		JSONObject custom = new JSONObject();
		custom.put("logoHtml", "");

		if (wymStyle != null) {
			custom.put("stylesheet", wymStyle);
		}

		// Use wymeditor
		renderSupport.addScript("jQuery('#%s').wymeditor(%s);",
				container.getClientId(), custom.toString());

	}
}
