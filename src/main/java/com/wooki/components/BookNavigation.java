package com.wooki.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import com.wooki.base.components.BookMenuItem;

@SupportsInformalParameters
public class BookNavigation {
	@Parameter
	private BookMenuItem left;

	/**
	 * Displayed as a small "h1"
	 */
	@Parameter
	private BookMenuItem center;

	@Parameter
	private BookMenuItem right;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport renderSupport;

	@SetupRender
	void startMenu(MarkupWriter writer) {
		writer.element("ul");
		resources.renderInformalParameters(writer);
		writer.getElement().addClassName("chapter-nav");
	}

	@BeginRender
	void renderItems(MarkupWriter writer) {
		writer.element("li", "id", "nav-left", "class", "chapter-nav-item");
		renderItem(left, writer);
		writer.end();

		writer.element("li", "id", "nav-right", "class", "chapter-nav-item");
		renderItem(right, writer);
		writer.end();

		writer.element("li", "id", "book-root", "class", "chapter-nav-item");
		writer.element("h1");
		renderItem(center, writer);
		writer.end();
		writer.end();
	}

	private void renderItem(BookMenuItem item, MarkupWriter writer) {
		if (item == null) {
			writer.writeRaw("&nbsp;");
			return;
		}
		
		String id = renderSupport.allocateClientId("chapter-nav-item-link");
		
		writer.element("a", "href", item.getLink().toString(), "id", id);
		writer.write(item.getName());
		writer.end();

		if (item.isConfirm()) {
			JSONObject params = new JSONObject();
			params.put("lnkId", id);

			renderSupport.addInit("initConfirm", params);
		}
	}

	@AfterRender
	void closeMenu(MarkupWriter writer) {
		writer.end();
	}

}
