package com.wooki.services.feeds.impl;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.services.ServicesMessages;
import com.wooki.services.feeds.AbstractActivityFeed;

public class ChapterActivityFeed extends AbstractActivityFeed<ChapterActivity>
{

    public ChapterActivityFeed(@Inject LinkSource linkSource, @Inject ServicesMessages messages)
    {
        super(linkSource, messages);
    }

    public String getTitle(ChapterActivity activity)
    {
        return messages.getMessages().format(
                getKeyForTitle(activity.getType().toString()),
                activity.getChapter().getBook().getTitle(),
                activity.getChapter().getTitle(),
                activity.getUser().getUsername());
    }

    public String getSummary(ChapterActivity activity)
    {
        return messages.getMessages().format(
                getKeyForSummary(activity.getType().toString()),
                activity.getChapter().getBook().getTitle(),
                activity.getChapter().getTitle(),
                activity.getUser().getUsername());
    }

    public Link getLink(ChapterActivity activity)
    {
        if (ChapterEventType.PUBLISHED.equals(activity.getType()))
        {
            org.apache.tapestry5.Link link = linkSource.createPageRenderLink(
                    "chapter/index",
                    true,
                    activity.getChapter().getBook().getId(),
                    activity.getChapter().getId());
            Link result = new Link();
            result.setHref(link.toAbsoluteURI());
            return result;
        }
        return null;
    }

}
