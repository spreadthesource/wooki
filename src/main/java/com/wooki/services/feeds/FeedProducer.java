package com.wooki.services.feeds;

import java.util.List;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.wooki.ActivityType;

/**
 * Defines a object that can generate a Feed with its description.
 * 
 * @author ccordenier
 * 
 */
public interface FeedProducer {


	/**
	 * Produces a feed, if ids are needed to access to the list related entities
	 * then pass it as parameters.
	 * 
	 * @param ids
	 * @return
	 */
	Feed produceFeed(ActivityType type, String id, String title, List<Link> altLinks, Long... ids);

}
