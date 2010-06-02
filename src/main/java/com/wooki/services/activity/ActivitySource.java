package com.wooki.services.activity;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

import com.wooki.domain.model.activity.Activity;

/**
 * This interface defines the methods that can be used to locate a activities for a given type of
 * display.
 * 
 * @author ccordenier
 */
@UsesMappedConfiguration(key = ActivitySourceType.class, value = ActivitySource.class)
public interface ActivitySource
{

    /**
     * Find the list of all activities for a given context.
     * 
     * @param context
     *            Ids used to find the target resource associated to the activities.
     * @return
     */
    List<Activity> listActivities(Long... context);

    /**
     * Find the list of activities before a given date.
     * 
     * @param context
     *            Ids used to find the target resource associated to the activities.
     * @return
     */
    List<Activity> listActivitiesForFeed(Long... context);

    /**
     * Find a list of activities in the provided range.
     * 
     * @param startIdx
     *            The start index use to build the query criteria.
     * @param range
     *            If range is equals to -1 then there get all the elements from startIdx.
     * @param context
     *            Ids used to find the target resource associated to the activities.
     * @return
     */
    List<Activity> listActivitiesRange(int startIdx, int range, Long... context);

}
