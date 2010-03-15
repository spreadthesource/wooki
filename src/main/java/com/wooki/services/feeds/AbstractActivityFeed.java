package com.wooki.services.feeds;

import java.lang.reflect.ParameterizedType;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.wooki.domain.model.activity.Activity;
import com.wooki.services.ServicesMessages;

@UsesMappedConfiguration(key = Class.class, value = AbstractActivityFeed.class)
public abstract class AbstractActivityFeed<T extends Activity> implements ActivityFeedWriter<T> {
	private final static String PREFIX = "feedwriter_";

	private final static String SUFFIX_TITLE = "_title";

	private final static String SUFFIX_SUMMARY = "_summary";

	private final String keyPrefixForTitle;

	private final String keyPrefixForSummary;

	protected final Class<T> activityType;

	protected final LinkSource linkSource;

	protected final ServicesMessages messages;

	public AbstractActivityFeed(@Inject LinkSource linkSource, @Inject ServicesMessages messages) {
		this.activityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.linkSource = linkSource;
		this.messages = messages;
		this.keyPrefixForTitle = PREFIX + this.activityType.getSimpleName() + "_";
		this.keyPrefixForSummary = PREFIX + this.activityType.getSimpleName() + "_";
	}

	public String getKeyForTitle(String event) {
		return keyPrefixForTitle + event + SUFFIX_TITLE;
	}

	public String getKeyForSummary(String event) {
		return keyPrefixForSummary + event + SUFFIX_TITLE;
	}

}
