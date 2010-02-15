package com.wooki.services.feeds.impl;

import java.io.IOException;

import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.services.feeds.ActivityFeedWriter;

public class AccountActivityFeedWriter implements ActivityFeedWriter<AccountActivity> {

	public String getTitle(AccountActivity activity) throws IOException {
		return activity.getUser().getFullname() + " has joined Wooki";
	}

	public String getSummary(AccountActivity activity) throws IOException {
		return activity.getUser().getFullname() + " created an account @" + activity.getCreationDate();
	}

}
