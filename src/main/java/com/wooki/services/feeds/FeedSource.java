package com.wooki.services.feeds;

import java.util.List;

import com.wooki.domain.model.activity.Activity;

/**
 * Use to extract the list of activities to fill a Feed.
 * 
 * @author ccordenier
 * 
 */
public interface FeedSource {

	List<Activity> getActivities(Long... ids);

}
