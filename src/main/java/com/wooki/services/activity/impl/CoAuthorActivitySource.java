package com.wooki.services.activity.impl;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.Defense;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.db.query.QueryFilterService;
import com.wooki.services.utils.DateUtils;

/**
 * This source provides all the activities of the co-authors for one user. Co-authors are people
 * that have book in common with the min user.
 * 
 * @author ccordenier
 */
public class CoAuthorActivitySource implements ActivitySource
{

    private final QueryFilterService filterService;

    private final ActivityDAO activityDao;

    public CoAuthorActivitySource(QueryFilterService filterService, ActivityDAO activityDao)
    {
        super();
        this.filterService = filterService;
        this.activityDao = activityDao;
    }

    public List<Activity> listActivities(Long... context)
    {
        checkContext(context);
        return this.activityDao.listCoauthorBookActivity(context[0], filterService
                .present());
    }

    public List<Activity> listActivitiesForFeed(Long... context)
    {
        checkContext(context);
        return this.activityDao.listCoauthorBookActivity(context[0], filterService
                .present(), filterService.createAfter(DateUtils.oneMonthAgo()));
    }

    public List<Activity> listActivitiesRange(int startIdx, int range, Long... context)
    {
        checkContext(context);
        return this.activityDao.listCoauthorBookActivity(context[0], filterService
                .present(), filterService.createAfter(DateUtils.oneMonthAgo()));
    }

    /**
     * Simply check context parameters to verify that everything required to create the query is
     * provided.
     */
    private void checkContext(Long... context)
    {
        Defense.notNull(context, "context");
        if (context.length != 1 || context[0] == null) { throw new IllegalArgumentException(
                "CoAuthorActivitySource lookup methods require the userf id as first and single parameter"); }
    }

}
