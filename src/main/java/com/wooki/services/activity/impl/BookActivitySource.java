package com.wooki.services.activity.impl;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.Defense;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.db.QueryFilterService;
import com.wooki.services.utils.DateUtils;

/**
 * This source provide all the activities on a given book.
 * 
 * @author ccordenier
 */
public class BookActivitySource implements ActivitySource
{

    private final QueryFilterService filterService;

    private final ActivityDAO activityDao;

    public BookActivitySource(QueryFilterService filterService, ActivityDAO activityDao)
    {
        super();
        this.filterService = filterService;
        this.activityDao = activityDao;
    }

    public List<Activity> listActivities(Long... context)
    {
        checkContext(context);
        return this.activityDao.listAllActivitiesOnBook(context[0], filterService.present());
    }

    public List<Activity> listActivitiesForFeed(Long... context)
    {
        checkContext(context);
        return this.activityDao.listAllActivitiesOnBook(
                context[0],
                filterService.present(),
                filterService.createAfter(DateUtils.oneMonthAgo()));
    }

    public List<Activity> listActivitiesRange(int startIdx, int range, Long... context)
    {
        checkContext(context);
        return this.activityDao.listAllActivitiesOnBook(
                context[0],
                filterService.present(),
                filterService.range(startIdx, range));
    }

    /**
     * Simply check context parameters to verify that everything required to create the query is
     * provided.
     */
    private void checkContext(Long... context)
    {
        Defense.notNull(context, "context");
        if (context.length != 1 || context[0] == null) { throw new IllegalArgumentException(
                "BookActivitySource lookup method require the book id as first and single parameter"); }
    }

}
