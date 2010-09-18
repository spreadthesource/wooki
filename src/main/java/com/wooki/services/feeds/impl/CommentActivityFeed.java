package com.wooki.services.feeds.impl;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;

public class CommentActivityFeed extends AbstractActivityFeed<CommentActivity>
{

    public String getTitle(CommentActivity activity)
    {
        return messages.getMessages().format(
                getKeyForTitle(activity.getType().toString()),
                activity.getUser().getUsername(),
                activity.getComment().getPublication().getChapter().getBook().getTitle());
    }

    public String getSummary(CommentActivity activity)
    {
        return messages.getMessages().format(
                getKeyForSummary(activity.getType().toString()),
                activity.getUser().getUsername(),
                activity.getComment().getPublication().getChapter().getBook().getTitle());
    }

    public Link getLink(CommentActivity activity)
    {
        if (CommentEventType.POST.equals(activity.getType()))
        {
            org.apache.tapestry5.Link link = linkSource.createPageRenderLink(
                    "book/issues",
                    true,
                    activity.getComment().getPublication().getChapter().getBook().getId());
            link.setAnchor("c" + activity.getComment().getId());
            Link result = new Link();
            result.setHref(link.toURI());
            return result;
        }
        return null;
    }

}
