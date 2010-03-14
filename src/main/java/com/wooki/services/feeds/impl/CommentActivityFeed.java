package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.pages.chapter.Issues;
import com.wooki.services.feeds.AbstractActivityFeed;

public class CommentActivityFeed extends AbstractActivityFeed<CommentActivity> {

	public CommentActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale, LinkSource linkSource) {
		super(urlConverter, locale, linkSource);
	}

	public String getTitle(CommentActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getUser().getUsername(),
				activity.getComment().getPublication().getChapter().getBook().getTitle());
	}

	public String getSummary(CommentActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getUser().getUsername(),
				activity.getComment().getPublication().getChapter().getBook().getTitle());
	}

	public Link getLink(CommentActivity activity) {
		if (CommentEventType.POST.equals(activity.getType())) {
			org.apache.tapestry5.Link link = linkSource.createPageRenderLink("chapter/issues", true, activity.getComment().getPublication().getChapter()
					.getBook().getId(), Issues.ALL);
			link.setAnchor("c" + activity.getComment().getId());
			Link result = new Link();
			result.setHref(link.toAbsoluteURI());
			return result;
		}
		return null;
	}

}
