package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.wooki.domain.model.activity.BookActivity;
import com.wooki.services.feeds.AbstractActivityFeed;

public class BookActivityFeed extends AbstractActivityFeed<BookActivity> {

	public BookActivityFeed(@Inject ClasspathURLConverter urlConverter, @Inject ThreadLocale locale) {
		super(urlConverter, locale, BookActivity.class.getSimpleName());
	}

	public String getTitle(BookActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getBook().getTitle(), activity.getUser().getUsername());
	}

	public String getSummary(BookActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getBook().getTitle(), activity.getUser().getUsername());
	}

}
