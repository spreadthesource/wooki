package com.wooki.pages.book;

import java.io.IOException;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.FeedException;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.FeedSource;

/**
 * Generate an return feeds.
 * 
 * @author ccordenier
 */
public class Rss
{

    @Inject
    private FeedSource feedSource;

    /**
     * Create the Atom feed of the book activity
     * 
     * @throws IOException
     * @throws FeedException
     * @throws IllegalArgumentException
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public Feed getFeed(Long bookId) throws IOException, IllegalArgumentException, FeedException
    {
        return feedSource.produceFeed(ActivitySourceType.BOOK, bookId);
    }
}
