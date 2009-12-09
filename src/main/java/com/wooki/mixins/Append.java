package com.wooki.mixins;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.Component;

/**
 * Append content instead of replacing it.
 * 
 * @author ccordenier
 * 
 */
@MixinAfter
public class Append {

	/** Insert Position */
	@Parameter(value = "bottom", defaultPrefix = BindingConstants.LITERAL)
	private String position;

	/** Client id of element to append result */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String to;

	@Inject
	private RenderSupport support;

	@InjectContainer
	private Form form;

	/**
	 * Submit the form via Ajax and handle result to append instead of replacing
	 * the whole content.
	 *
	 */
	@AfterRender
	public void append() {
		ComponentResources formResources = Component.class.cast(form)
				.getComponentResources();
		if (formResources.isBound("zone")) {
			throw new IllegalStateException(
					"'Append' mixin cannot be used if 'zone' parameter is set on form");
		}
		Link link = formResources.createFormEventLink(EventConstants.ACTION);
		support.addInit("appendToZone", link.toAbsoluteURI(), form
				.getClientId(), to, position);
	}

	/**
	 * Generate an errors message that will appear on the client side.
	 *
	 * @return
	 */
	@OnEvent(value = EventConstants.FAILURE)
	public Object checkErrors() {
		JSONObject result = new JSONObject();
		StringBuffer buff = new StringBuffer();
		List<String> errors = form.getDefaultTracker().getErrors();
		if (!errors.isEmpty()) {
			buff.append("<ul>");
			for (String error : errors) {
				buff.append("<li>").append(error).append("</li>");
			}
			buff.append("</ul>");
			result.put("errors", buff.toString());
		} else {
			buff.append("Unexcepted error");
		}
		return result;
	}

}
