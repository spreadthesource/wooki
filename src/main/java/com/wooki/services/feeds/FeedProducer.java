package com.wooki.services.feeds;


import com.sun.syndication.feed.atom.Feed;

/**
 * Use to extract the list of activities and information related to Feed.
 * 
 * @author ccordenier
 */
public interface FeedProducer
{

    /**
     * Must produce the Feed, context can be passed to find the resources targeted by the generated
     * feed.
     * @param context TODO
     * 
     * @return
     */
    Feed produce(Long... context);

}
