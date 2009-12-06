package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * This component send a request to the server and remove an element on the
 * client side if the server returns true.
 * 
 * @author ccordenier
 * 
 */
public class ClickAndRemove extends AbstractLink {

	private static final String CLICK_AND_REMOVE = "clickAndRemove";

	/** The id of entity to remove */
	@Parameter(required = true, allowNull = false)
	private Long entityId;

	/** The prefix associated to element to remove */
	@Parameter(value = "ent-", defaultPrefix = BindingConstants.LITERAL)
	private String domPrefix;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	void beginRender(MarkupWriter writer) {
		if (isDisabled())
			return;
		Link link = resources.createEventLink(CLICK_AND_REMOVE, this.entityId);
		writeLink(writer, link);
	}

	void afterRender(MarkupWriter writer) {
		if (isDisabled())
			return;
		writer.end(); // <a>
	}

	@AfterRender
	public void initClickAndPlay() {
		Link link = resources.createEventLink(CLICK_AND_REMOVE, this.entityId);
		JSONObject data = new JSONObject();
		data.put("elt", this.getClientId());
		data.put("url", link.toAbsoluteURI());
		data.put("toRemove", domPrefix + entityId);
		support.addInit("initClickAndRemove", data);
	}

	@OnEvent(value = CLICK_AND_REMOVE)
	public Object clickAndRemove(Long entityId) {
		final JSONObject data = new JSONObject();
		try {
			resources.triggerEvent("remove", new Object[] { entityId }, null);
			data.put("result", true);
		} catch (Exception ex) {
			data.put("result", false);
		}
		return data;
	}

}
