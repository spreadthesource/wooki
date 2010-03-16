package com.wooki.services.feeds;

import com.sun.syndication.feed.atom.Feed;
import com.wooki.ActivityType;

/**
 * Defines a object that can generate a Feed with its description.
 * 
 * @author ccordenier
 * 
 */
public interface FeedSource {

	/**
	 * Produces a feed, if ids are needed to access to the list related entities
	 * then pass it as parameters.
	 * 
	 * @param ids
	 * @return
	 */
	Feed produceFeed(ActivityType type, Long... context);

}
