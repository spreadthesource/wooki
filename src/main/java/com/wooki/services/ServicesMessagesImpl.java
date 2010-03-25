package com.wooki.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.services.MessagesBundle;
import org.apache.tapestry5.internal.services.MessagesSource;
import org.apache.tapestry5.internal.services.MessagesSourceImpl;
import org.apache.tapestry5.internal.util.URLChangeTracker;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.messages.PropertiesFileParser;

public class ServicesMessagesImpl implements ServicesMessages
{

    private final MessagesBundle bundle;

    private final MessagesSource source;

    private final ThreadLocale locale;

    private final Resource appCatalogResource;

    public ServicesMessagesImpl(
            @Symbol(SymbolConstants.APPLICATION_CATALOG) final Resource appCatalogResource,
            @Inject ClasspathURLConverter urlConverter, @Inject ThreadLocale locale,
            @Inject LinkSource linkSource, @Inject PropertiesFileParser propertyFileParser)
    {
        URLChangeTracker tracker = new URLChangeTracker(urlConverter);
        this.appCatalogResource = appCatalogResource;
        this.source = new MessagesSourceImpl(tracker, propertyFileParser);
        this.locale = locale;
        this.bundle = new MessagesBundle()
        {
            private final Resource resource;
            {
                this.resource = new ClasspathResource("com/wooki/services/wooki-services");
            }

            public MessagesBundle getParent()
            {
                if (appCatalogResource.exists()) { return rootBundle(); }
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
    }

    public Messages getMessages()
    {
        return source.getMessages(bundle, locale.getLocale());
    }

    public void checkForUpdates()
    {
        source.checkForUpdates();
    }

    private MessagesBundle rootBundle()
    {
        return new MessagesBundle()
        {
            public Resource getBaseResource()
            {
                return appCatalogResource;
            }

            public Object getId()
            {
                return appCatalogResource.getPath();
            }

            public MessagesBundle getParent()
            {
                return null;
            }
        };
    }

}
