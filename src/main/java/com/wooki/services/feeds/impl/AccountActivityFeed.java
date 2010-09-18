package com.wooki.services.feeds.impl;

import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;

public class AccountActivityFeed extends AbstractActivityFeed<AccountActivity>
{

    public String getTitle(AccountActivity activity)
    {
        return messages.getMessages().format(
                getKeyForTitle(activity.getType().toString()),
                activity.getUser().getUsername(),
                activity.getUser().getFullname());
    }

    public String getSummary(AccountActivity activity)
    {
        return messages.getMessages().format(
                getKeyForSummary(activity.getType().toString()),
                activity.getUser().getUsername(),
                activity.getUser().getFullname());
    }

    public Link getLink(AccountActivity activity)
    {
        if (AccountEventType.JOIN.equals(activity.getType()))
        {
            org.apache.tapestry5.Link link = linkSource.createPageRenderLink(
                    "index",
                    true,
                    activity.getUser().getUsername());
            Link result = new Link();
            result.setHref(link.toURI());
            return result;
        }
        return null;
    }

}
