package com.wooki.services.feeds.impl;

import java.io.IOException;

import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.services.feeds.ActivityFeedWriter;

public class CommentActivityFeedWriter implements ActivityFeedWriter<CommentActivity> {

	public String getTitle(CommentActivity activity) throws IOException {
		return activity.getUser().getFullname() + " has done something with comments...";
	}

	public String getSummary(CommentActivity activity) throws IOException {
		return activity.getUser().getFullname() + " ...";
	}

}
