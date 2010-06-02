package com.wooki.services.activity.impl;

import java.util.List;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.db.query.QueryFilterService;
import com.wooki.services.utils.DateUtils;

/**
 * This source provides join user activities.
 * 
 * @author ccordenier
 */
public class AccountActivitySource implements ActivitySource
{

    private final QueryFilterService filterService;

    private final ActivityDAO activityDao;

    public AccountActivitySource(QueryFilterService filterService, ActivityDAO activityDao)
    {
        super();
        this.filterService = filterService;
        this.activityDao = activityDao;
    }

    public List<Activity> listActivities(Long... context)
    {
        return activityDao.listAccountActivity(filterService.present());
    }

    public List<Activity> listActivitiesForFeed(Long... context)
    {
        return activityDao.listAccountActivity(filterService.present(), filterService
                .createAfter(DateUtils.oneMonthAgo()));
    }

    public List<Activity> listActivitiesRange(int startIdx, int range, Long... context)
    {
        return activityDao.listAccountActivity(filterService.present(), filterService
                .range(startIdx, range));
    }
}
