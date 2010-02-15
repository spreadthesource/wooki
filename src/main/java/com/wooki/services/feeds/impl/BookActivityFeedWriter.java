package com.wooki.services.feeds.impl;

import java.io.IOException;

import com.wooki.domain.model.activity.BookActivity;
import com.wooki.services.feeds.ActivityFeedWriter;

public class BookActivityFeedWriter implements ActivityFeedWriter<BookActivity> {

	public String getTitle(BookActivity activity) throws IOException {
		return activity.getUser().getFullname() + " has done something on the book...";
	}

	public String getSummary(BookActivity activity) throws IOException {
		return activity.getUser().getFullname() + " ...";
	}

}
