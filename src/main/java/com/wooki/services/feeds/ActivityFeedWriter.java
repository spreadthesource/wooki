package com.wooki.services.feeds;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.Activity;

@UsesMappedConfiguration(key = Class.class, value = ActivityFeedWriter.class)
public interface ActivityFeedWriter<T extends Activity>
{
    /**
     * @param activity
     *            the activity to output
     */
    public String getTitle(T activity);

    /**
     * @param activity
     *            the activity to output
     */
    public String getSummary(T activity);

    /**
     * Return the link associated to the type of activity.
     * 
     * @param activity
     * @return
     */
    public Link getLink(T activity);
}
