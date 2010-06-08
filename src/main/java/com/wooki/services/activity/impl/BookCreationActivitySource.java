package com.wooki.services.activity.impl;

import java.util.List;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.db.QueryFilterService;
import com.wooki.services.utils.DateUtils;

/**
 * This source provide all the book creation activity.
 * 
 * @author ccordenier
 */
public class BookCreationActivitySource implements ActivitySource
{
    private final QueryFilterService filterService;

    private final ActivityDAO activityDao;

    public BookCreationActivitySource(QueryFilterService filterService, ActivityDAO activityDao)
    {
        super();
        this.filterService = filterService;
        this.activityDao = activityDao;
    }

    public List<Activity> listActivities(Long... context)
    {
        return activityDao.listBookCreationActivity(filterService.present());
    }

    public List<Activity> listActivitiesForFeed(Long... context)
    {
        return activityDao.listBookCreationActivity(
                filterService.present(),
                filterService.createAfter(DateUtils.oneMonthAgo()));
    }

    public List<Activity> listActivitiesRange(int startIdx, int range, Long... context)
    {
        return activityDao.listBookCreationActivity(
                filterService.present(),
                filterService.range(startIdx, range));
    }

}
