package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.services.feeds.AbstractActivityFeed;

public class AccountActivityFeed extends AbstractActivityFeed<AccountActivity> {

	public AccountActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale, LinkSource linkSource) {
		super(urlConverter, locale, linkSource);
	}

	public String getTitle(AccountActivity activity) throws IOException {
		return getMessages().format(getKeyForTitle(activity.getType().toString()), activity.getUser().getUsername(), activity.getUser().getFullname());
	}

	public String getSummary(AccountActivity activity) throws IOException {
		return getMessages().format(getKeyForSummary(activity.getType().toString()), activity.getUser().getUsername(), activity.getUser().getFullname());
	}

	public Link getLink(AccountActivity activity) {
		if (AccountEventType.JOIN.equals(activity.getType())) {
			org.apache.tapestry5.Link link = linkSource.createPageRenderLink("index", true, activity.getUser().getUsername());
			Link result = new Link();
			result.setHref(link.toAbsoluteURI());
			return result;
		}
		return null;
	}

}
