package com.wooki.components;

import java.util.List;

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
public class BookMenu {

	@Parameter
	private List<BookMenuItem> items;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport renderSupport;

	@SetupRender
	boolean startMenu(MarkupWriter writer) {
		if (items.size() == 0)
			return false;

		writer.element("ul");
		resources.renderInformalParameters(writer);
		writer.getElement().addClassName("book-menu", "shadowed");

		return true;
	}

	@BeginRender
	void renderItems(MarkupWriter writer) {
		int i = 0;
		for (BookMenuItem item : items) {
			i++;
			writer.element("li");
			writer.element("a", "href", item.getLink().toString(), "class", "bookmenuitem");
			if (item.isConfirm()) {
				String id = renderSupport.allocateClientId("bookmenuitem");
				writer.attributes("id", id);

				JSONObject params = new JSONObject();
				params.put("lnkId", id);

				renderSupport.addInit("initConfirm", params);
			}

			writer.write(item.getName());

			writer.end();

			if (i == items.size()) {
				writer.getElement().addClassName("last");
			}
			writer.end();
		}
	}

	@AfterRender
	void closeMenu(MarkupWriter writer) {
		writer.end();
	}

}
