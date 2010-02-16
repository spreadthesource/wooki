package com.wooki.services.feeds;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.NotLazy;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.services.InvalidationEventHub;
import org.apache.tapestry5.services.UpdateListener;

import com.wooki.domain.model.activity.Activity;

@UsesMappedConfiguration(key = Class.class, value = ActivityFeed.class)
public interface ActivityFeed<T extends Activity> extends UpdateListener {
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

    @NotLazy
    public InvalidationEventHub getInvalidationEventHub();
}
