package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.services.feeds.AbstractActivityFeed;

public class ChapterActivityFeed extends AbstractActivityFeed<ChapterActivity> {

	public ChapterActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale, LinkSource linkSource) {
		super(urlConverter, locale, linkSource);
	}

	public String getTitle(ChapterActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getChapter().getBook().getTitle(),
				activity.getChapter().getTitle(), activity.getUser().getUsername());
	}

	public String getSummary(ChapterActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getChapter().getBook().getTitle(),
				activity.getChapter().getTitle(), activity.getUser().getUsername());
	}

	public Link getLink(ChapterActivity activity) {
		if (ChapterEventType.PUBLISHED.equals(activity.getType())) {
			org.apache.tapestry5.Link link = linkSource.createPageRenderLink("chapter/index", true, activity.getChapter().getBook().getId(), activity
					.getChapter().getId());
			Link result = new Link();
			result.setHref(link.toAbsoluteURI());
			return result;
		}
		return null;
	}

}
