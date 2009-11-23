package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

@IncludeJavaScriptLibrary("context:static/js/jquery-ui-1.7.2.custom.min.js")
public class CommentDialog implements ClientElement {
	@Inject
	private RenderSupport support;

	@Inject
	private ComponentResources resources;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	@BeginRender
	void startDiv(MarkupWriter writer) {
		writer.element("div", "id", getClientId(), "class", "dialog-container");
		writer.element("div", "class", "dialog-content");
	}

	@AfterRender
	void declareDialog(MarkupWriter writer) {
		writer.end();
		writer.end();
		support.addScript("jQuery('#%s').dialog({" +
				"modal: true, width: 780, minHeight:30, autoOpen: false" +
				"});", resources.getId());
	}

	public String getClientId() {
		if (clientId != null)
			return support.allocateClientId(clientId);
		return support.allocateClientId(resources);
	}
}
