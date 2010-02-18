package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;

import com.wooki.AppendPosition;
import com.wooki.MoreEventResult;
import com.wooki.WookiEventConstants;

/**
 * This link will be used to append information to a zone. The position can be
 * on top or at the bottom of the HTML element associated to the link.
 * 
 * @author ccordenier
 * 
 */
public class MoreLink extends AbstractLink {

	/**
	 * Specify the first page index.
	 */
	@Parameter
	private int first;

	/**
	 * The name of the element to update.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	/**
	 * Where should the content be append : top or botttom
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "bottom")
	private AppendPosition position;

	/**
	 * The class of the element that shows the ajax loader image
	 */
	@Parameter(value = "load-more", defaultPrefix = BindingConstants.LITERAL)
	private String loaderClass;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport renderSupport;

	@Inject
	private TypeCoercer typeCoercer;

	@Inject
	private PageRenderQueue pageRenderQueue;

	private String loader;

	@BeginRender
	void initMoreLink(MarkupWriter writer) {
		if (isDisabled())
			return;

		Link link = resources.createEventLink(EventConstants.ACTION, first);
		writer.element("div", "class", "more-link");
		writeLink(writer, link);

		loader = renderSupport.allocateClientId("loader");

		JSONObject data = new JSONObject();
		data.put("elt", this.getClientId());
		data.put("position", this.position.toString());
		data.put("zone", this.zone);
		data.put("loader", loader);
		data.put("url", link.toAbsoluteURI());
		renderSupport.addInit("initMoreLink", data);
	}

	@AfterRender
	void afterRender(MarkupWriter writer) {
		if (isDisabled())
			return;
		writer.end(); // <a>
		
		writer.element("div", "id", loader, "class", this.loaderClass, "style", "display:none;");
		writer.end(); // <div> loader

		writer.end(); // <div>
	}

	/**
	 * When more is used via Javascript, href attribute is update via this
	 * method.
	 * 
	 * @return the object to render.
	 */
	@SuppressWarnings("unchecked")
	@OnEvent(value = EventConstants.ACTION)
	public Object updateContext(final int page) {

		// Get the new context
		final Holder<MoreEventResult> holder = Holder.create();
		ComponentEventCallback callback = new ComponentEventCallback() {
			public boolean handleResult(Object result) {
				if (result == null) {
					return true;
				}
				holder.put(typeCoercer.coerce(result, MoreEventResult.class));
				return true;
			}
		};

		resources.triggerEvent(WookiEventConstants.UPDATE_MORE_CONTEXT, new Object[] { page }, callback);

		// Add results in JSON reply
		pageRenderQueue.addPartialMarkupRendererFilter(new PartialMarkupRendererFilter() {

			public void renderMarkup(MarkupWriter writer, JSONObject reply, PartialMarkupRenderer renderer) {
				if (holder.hasValue()) {
					Link link = resources.createEventLink(EventConstants.ACTION, page + 1);
					reply.put("href", link.toAbsoluteURI());
					reply.put("hasMore", holder.get().hasMore());
				} else {
					reply.put("empty", true);
				}
				renderer.renderMarkup(writer, reply);
			}
		});

		// Check if there is more data to render
		if (holder.hasValue()) {
			return holder.get().getRenderable();
		}

		return null;
	}

}
