package com.wooki.base;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentSource;

public class AbstractPage {

	@Inject
	private ComponentSource source;

	@Inject
	private ComponentResources resources;

	public void addFeedLink(String pageName, String title, MarkupWriter writer, Object... context) {
		Link feedLink = this.source.getPage(pageName).getComponentResources().createEventLink("feed", context);
		writeFeed(title, writer, feedLink);
	}

	public void addFeedLink(String title, MarkupWriter writer, Object... context) {
		Link feedLink = resources.createEventLink("feed", context);
		writeFeed(title, writer, feedLink);
	}

	private void writeFeed(String title, MarkupWriter writer, Link feedLink) {
		Element head = writer.getDocument().find("html/head");
		head.element("link", "type", "application/atom+xml", "title", title, "rel", "alternate", "href", feedLink.toAbsoluteURI());
	}

}
