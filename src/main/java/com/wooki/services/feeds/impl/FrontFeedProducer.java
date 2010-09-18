package com.wooki.services.feeds.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.wooki.services.EnumServiceLocator;
import com.wooki.services.activity.ActivitySourceType;

/**
 * Produces the feed for a single book.
 * 
 * @author ccordenier
 */
public class FrontFeedProducer extends AbstractFeedProducer
{

    @Inject
    private EnumServiceLocator locator;

    /**
     * Read book definition and generate corresponding feed.
     */
    public Feed produce(Long... context)
    {

        String title = messages.getMessages().get("recent-creation-activity");
        String id = "wooki-public";

        List<Link> alternateLinks = new ArrayList<Link>();

        Link linkToSelf = new Link();
        linkToSelf.setHref(lnkSource.createPageRenderLink("index", false).toURI());
        linkToSelf.setTitle(messages.getMessages().get("front-feed-title"));

        alternateLinks.add(linkToSelf);

        return super.fillFeed(id, title, alternateLinks, locator.getService(
                ActivitySourceType.BOOK_CREATION).listActivitiesForFeed(context));
    }

}
