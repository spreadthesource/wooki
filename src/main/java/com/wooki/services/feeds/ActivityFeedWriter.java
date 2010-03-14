package com.wooki.services.feeds;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.Activity;

@UsesMappedConfiguration(key = Class.class, value = ActivityFeedWriter.class)
public interface ActivityFeedWriter<T extends Activity> {
	/**
	 * @param activity
	 *            the activity to output
	 * @throws RuntimeException
	 *             if the value can not handled
	 */
	public String getTitle(T activity) throws IOException;

	/**
	 * @param activity
	 *            the activity to output
	 * @throws RuntimeException
	 *             if the value can not handled
	 */
	public String getSummary(T activity) throws IOException;

	/**
	 * Return the link associated to the type of activity.
	 * 
	 * @param activity
	 * @return
	 */
	public Link getLink(T activity);
}
