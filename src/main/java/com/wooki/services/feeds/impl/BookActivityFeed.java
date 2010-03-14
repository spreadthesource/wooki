package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.services.feeds.AbstractActivityFeed;

public class BookActivityFeed extends AbstractActivityFeed<BookActivity> {

	public BookActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale, LinkSource linkSource) {
		super(urlConverter, locale, linkSource);
	}

	public String getTitle(BookActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getBook().getTitle(), activity.getUser().getUsername());
	}

	public String getSummary(BookActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getBook().getTitle(), activity.getUser().getUsername());
	}

	public Link getLink(BookActivity activity) {
		if (BookEventType.CREATE.equals(activity.getType())) {
			org.apache.tapestry5.Link link = linkSource.createPageRenderLink("book/index", true, activity.getBook().getId());
			Link result = new Link();
			result.setHref(link.toAbsoluteURI());
			return result;
		}
		return null;
	}

}
