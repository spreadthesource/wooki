package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

public class DialogLink implements ClientElement {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	@Parameter(required = true)
	private String dialog;

	@Inject
	private RenderSupport support;

	@Inject
	private ComponentResources resources;

	@BeginRender
	void startDiv(MarkupWriter writer) {
		writer.element("a", "id", getClientId(), "href", "#");
	}

	@AfterRender
	void declareDialog(MarkupWriter writer) {
		writer.end();
		support.addInit("openJQueryDialogOnClick", getClientId(), dialog);
	}

	public String getClientId() {
		if (clientId == null) {
			clientId = support.allocateClientId(resources);
		}
		return this.clientId;
	}
}
