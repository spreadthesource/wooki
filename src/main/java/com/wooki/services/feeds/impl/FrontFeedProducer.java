package com.wooki.services.feeds.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.internal.services.LinkSource;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.ServicesMessages;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.feeds.AbstractFeedProducer;
import com.wooki.services.feeds.ActivityFeedWriter;

/**
 * Produces the feed for a single book.
 * 
 * @author ccordenier
 */
public class FrontFeedProducer extends AbstractFeedProducer
{

    public FrontFeedProducer(ServicesMessages messages, LinkSource lnkSource,
            ActivityFeedWriter<Activity> activityFeed, ActivitySource source)
    {
        super(messages, lnkSource, activityFeed, source);
    }

    /**
     * Read book definition and generate corresponding feed.
     */
    public Feed produce(Long... context)
    {

        String title = messages.getMessages().get("recent-creation-activity");
        String id = "wooki-public";

        List<Link> alternateLinks = new ArrayList<Link>();

        Link linkToSelf = new Link();
        linkToSelf.setHref(lnkSource.createPageRenderLink("index", false).toAbsoluteURI());
        linkToSelf.setTitle(messages.getMessages().get("front-feed-title"));

        alternateLinks.add(linkToSelf);

        return super.fillFeed(id, title, alternateLinks, source.listActivitiesForFeed(context));
    }

}
