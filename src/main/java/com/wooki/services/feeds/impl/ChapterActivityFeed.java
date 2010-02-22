package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.services.feeds.AbstractActivityFeed;

public class ChapterActivityFeed extends AbstractActivityFeed<ChapterActivity> {

	public ChapterActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale) {
		super(urlConverter, locale, ChapterActivity.class.getSimpleName());
	}

	public String getTitle(ChapterActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getChapter().getBook().getTitle(),
				activity.getChapter().getTitle(), activity.getUser().getUsername());
	}

	public String getSummary(ChapterActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getChapter().getBook().getTitle(),
				activity.getChapter().getTitle(), activity.getUser().getUsername());
	}

}
