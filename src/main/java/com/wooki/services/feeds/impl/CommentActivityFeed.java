package com.wooki.services.feeds.impl;

import java.io.IOException;

import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.services.feeds.AbstractActivityFeed;

public class CommentActivityFeed extends AbstractActivityFeed<CommentActivity>
{

    public CommentActivityFeed(ClasspathURLConverter urlConverter, ThreadLocale locale)
    {
        super(urlConverter, locale, CommentActivity.class.getSimpleName());
    }

    public String getTitle(CommentActivity activity) throws IOException
    {
        return activity.getUser().getFullname() + " has done something with comments...";
    }

    public String getSummary(CommentActivity activity) throws IOException
    {
        return activity.getUser().getFullname() + " ...";
    }

}
