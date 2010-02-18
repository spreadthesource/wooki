package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.services.feeds.AbstractActivityFeed;

public class AccountActivityFeed extends AbstractActivityFeed<AccountActivity>
{
    public AccountActivityFeed(@Inject ClasspathURLConverter urlConverter,
            @Inject ThreadLocale locale)
    {
        super(urlConverter, locale, AccountActivity.class.getSimpleName());
    }

    public String getTitle(AccountActivity activity) throws IOException
    {
        // return getMessages().format(key, args)
        return null;
    }

    public String getSummary(AccountActivity activity) throws IOException
    {
        return activity.getUser().getFullname() + " created an account @"
                + activity.getCreationDate();
    }

}
