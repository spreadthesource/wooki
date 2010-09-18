package com.wooki.services.feeds.impl;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;

public class BookActivityFeed extends AbstractActivityFeed<BookActivity>
{

    public String getTitle(BookActivity activity)
    {
        return messages.getMessages().format(
                getKeyForTitle(activity.getType().toString()),
                activity.getBook().getTitle(),
                activity.getUser().getUsername());
    }

    public String getSummary(BookActivity activity)
    {
        return messages.getMessages().format(
                getKeyForSummary(activity.getType().toString()),
                activity.getBook().getTitle(),
                activity.getUser().getUsername());
    }

    public Link getLink(BookActivity activity)
    {
        if (BookEventType.CREATE.equals(activity.getType()))
        {
            org.apache.tapestry5.Link link = linkSource.createPageRenderLink(
                    "book/index",
                    true,
                    activity.getBook().getId());
            Link result = new Link();
            result.setHref(link.toURI());
            return result;
        }
        return null;
    }

}
