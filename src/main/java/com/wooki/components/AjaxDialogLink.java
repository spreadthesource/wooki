package com.wooki.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Make a link to update a zone and popup a dialog box
 */
public class AjaxDialogLink extends DialogLink {
	@Parameter(required = true)
	private String zone;

	@Parameter
	private Object[] context;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	@Override
	@AfterRender
	void declareDialog(MarkupWriter writer) {
		writer.end();

		Link link = resources.createEventLink(EventConstants.ACTION, context);

		support.addInit("openJQueryAjaxDialogOnClick", getClientId(), zone, getDialog(), link.toAbsoluteURI());
	}
}
