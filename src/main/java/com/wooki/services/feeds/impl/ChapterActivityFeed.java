package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.services.feeds.AbstractActivityFeed;

public class ChapterActivityFeed extends AbstractActivityFeed<ChapterActivity>
{

    public ChapterActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale)
    {
        super(urlConverter, locale, ChapterActivity.class.getSimpleName());
    }

    public String getTitle(ChapterActivity activity) throws IOException
    {
        return activity.getUser().getFullname() + " has "
                + getMessages().get(activity.getType().toString()) + " on the chapter...";
    }

    public String getSummary(ChapterActivity activity) throws IOException
    {
        return activity.getUser().getFullname() + " ...";
    }

}
