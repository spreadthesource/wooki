package com.wooki.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;

/**
 * This mixins implements a simple show hide effect.
 * 
 * @author ccordenier
 * 
 */
public class ShowHideOnClick {

	/**
	 * This parameter is the element to display on click.
	 * 
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String toShow;

	/**
	 * This is the id of the element that will return to initial state on click.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String hideLnkId;

	/**
	 * Effect duration.
	 * 
	 */
	@Parameter(required = true, allowNull = false, value = "200")
	private int duration;

	/**
	 * Reset the form on hide event.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String resetFormClass;
	
	@InjectContainer
	private AbstractLink showLnkId;

	@Inject
	private RenderSupport support;

	/**
	 * Generate Javascript method call.
	 * 
	 */
	@AfterRender
	public void initShowHideEffect() {

		JSONObject data = new JSONObject();
		data.put("showLnkId", this.showLnkId.getClientId());
		data.put("toShow", this.toShow);
		data.put("hideLnkId", this.hideLnkId);
		data.put("duration", this.duration);

		// Also reset form if the link is inside a form
		if (this.resetFormClass != null) {
			data.put("formClass", this.resetFormClass);
		}

		support.addInit("initShowHideEffect", data);
	}

}
