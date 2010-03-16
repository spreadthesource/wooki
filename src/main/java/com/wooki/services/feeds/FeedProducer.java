package com.wooki.services.feeds;

import com.sun.syndication.feed.atom.Feed;

/**
 * Use to extract the list of activities and information related to Feed.
 * 
 * @author ccordenier
 * 
 */
public interface FeedProducer {

	Feed produce(Long... context);

}
