package com.wooki.services.feeds.impl;

import java.lang.reflect.ParameterizedType;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.services.RequestGlobals;

import com.wooki.domain.model.activity.Activity;
import com.wooki.services.ServicesMessages;
import com.wooki.services.feeds.ActivityFeedWriter;

@UsesMappedConfiguration(key = Class.class, value = AbstractActivityFeed.class)
public abstract class AbstractActivityFeed<T extends Activity> implements ActivityFeedWriter<T>
{
    private final static String PREFIX = "feedwriter_";

    private final static String SUFFIX_TITLE = "_title";

    private final static String SUFFIX_SUMMARY = "_summary";

    private final String keyPrefixForTitle;

    private final String keyPrefixForSummary;

    protected final Class<T> activityType;

    @Inject
    protected LinkSource linkSource;

    @Inject
    protected ServicesMessages messages;

    @Inject
    protected RequestGlobals globals;
    
    public AbstractActivityFeed()
    {
        this.activityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.keyPrefixForTitle = PREFIX + this.activityType.getSimpleName() + "_";
        this.keyPrefixForSummary = PREFIX + this.activityType.getSimpleName() + "_";
    }

    public String getKeyForTitle(String event)
    {
        return keyPrefixForTitle + event + SUFFIX_TITLE;
    }

    public String getKeyForSummary(String event)
    {
        return keyPrefixForSummary + event + SUFFIX_TITLE;
    }
    
    protected String toUrl(Link link) {
        HttpServletRequest request = globals.getHTTPServletRequest();
        String result = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + link.toURI();
        return result;
    }

}
