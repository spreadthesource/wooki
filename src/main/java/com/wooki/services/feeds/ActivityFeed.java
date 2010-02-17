package com.wooki.services.feeds;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.wooki.domain.model.activity.Activity;

@UsesMappedConfiguration(key = Class.class, value = ActivityFeed.class)
public interface ActivityFeed<T extends Activity> {
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

}
