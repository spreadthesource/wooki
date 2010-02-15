package com.wooki.services.feeds.impl;

import java.io.IOException;

import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.services.feeds.ActivityFeedWriter;

public class ChapterActivityFeedWriter implements ActivityFeedWriter<ChapterActivity> {

	public String getTitle(ChapterActivity activity) throws IOException {
		return activity.getUser().getFullname() + " has done something on the chapter...";
	}

	public String getSummary(ChapterActivity activity) throws IOException {
		return activity.getUser().getFullname() + " ...";
	}

}
