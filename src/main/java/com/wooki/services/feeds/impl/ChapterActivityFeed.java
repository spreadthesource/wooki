package com.wooki.services.feeds.impl;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;

public class ChapterActivityFeed extends AbstractActivityFeed<ChapterActivity>
{

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
            result.setHref(link.toURI());
            return result;
        }
        return null;
    }

}
