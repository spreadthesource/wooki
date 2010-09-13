package com.wooki.services.feeds.impl;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.pages.chapter.Issues;
import com.wooki.services.ServicesMessages;

public class CommentActivityFeed extends AbstractActivityFeed<CommentActivity>
{

    public CommentActivityFeed(@Inject LinkSource linkSource, @Inject ServicesMessages messages)
    {
        super(linkSource, messages);
    }

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
            result.setHref(link.toAbsoluteURI());
            return result;
        }
        return null;
    }

}
