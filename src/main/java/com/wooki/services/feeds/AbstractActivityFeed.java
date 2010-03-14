package com.wooki.services.feeds;

import java.lang.reflect.ParameterizedType;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.services.MessagesBundle;
import org.apache.tapestry5.internal.services.MessagesSource;
import org.apache.tapestry5.internal.services.MessagesSourceImpl;
import org.apache.tapestry5.internal.util.URLChangeTracker;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.UpdateListener;

import com.wooki.domain.model.activity.Activity;

@UsesMappedConfiguration(key = Class.class, value = AbstractActivityFeed.class)
public abstract class AbstractActivityFeed<T extends Activity> implements ActivityFeedWriter<T>,
        UpdateListener

{
    private final static String PREFIX = "feedwriter_";

    private final static String SUFFIX_TITLE = "_title";

    private final static String SUFFIX_SUMMARY = "_summary";

    private final MessagesBundle bundle;

    private final MessagesSource source;

    private final ThreadLocale locale;

    private final String keyPrefixForTitle;

    private final String keyPrefixForSummary;

	protected final Class<T> activityType;
    
	protected final LinkSource linkSource;
	
    public AbstractActivityFeed(@Inject ClasspathURLConverter urlConverter,
            @Inject ThreadLocale locale, @Inject LinkSource linkSource)
    {
    	this.activityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        URLChangeTracker tracker = new URLChangeTracker(urlConverter);
        this.source = new MessagesSourceImpl(tracker);
        this.linkSource = linkSource;
        this.locale = locale;
        this.bundle = new MessagesBundle()
        {
            private final Resource resource;

            {
                this.resource = new ClasspathResource(
                        "com/wooki/services/utils/LastActivityStrings");
            }

            public MessagesBundle getParent()
            {
                return null;
            }

            public Object getId()
            {
                return resource.getPath();
            }

            public Resource getBaseResource()
            {
                return this.resource;
            }
        };

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

    public Messages getMessages()
    {
        return source.getMessages(bundle, locale.getLocale());
    }

    public void checkForUpdates()
    {
        source.checkForUpdates();
    }

}
