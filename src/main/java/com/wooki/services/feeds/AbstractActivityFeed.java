package com.wooki.services.feeds;

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

    public AbstractActivityFeed(@Inject ClasspathURLConverter urlConverter,
            @Inject ThreadLocale locale, String className)
    {
        URLChangeTracker tracker = new URLChangeTracker(urlConverter);
        this.source = new MessagesSourceImpl(tracker);
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

        this.keyPrefixForTitle = PREFIX + className + SUFFIX_TITLE;
        this.keyPrefixForSummary = PREFIX + className + SUFFIX_SUMMARY;
    }

    public String getKeyForTitle()
    {
        return keyPrefixForTitle;
    }

    public String getKeyForSummary()
    {
        return keyPrefixForSummary;
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
