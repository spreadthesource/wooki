package com.wooki.services.activity.impl;

import java.util.List;

import org.apache.tapestry5.ioc.internal.util.Defense;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.db.QueryFilterService;
import com.wooki.services.utils.DateUtils;

/**
 * This source provides all the activities of a user.
 * 
 * @author ccordenier
 */
public class UserPublicActivitySource implements ActivitySource
{

    private final ActivityDAO dao;

    private final QueryFilterService filterService;

    public UserPublicActivitySource(ActivityDAO dao, QueryFilterService filterService)
    {
        super();
        this.filterService = filterService;
        this.dao = dao;
    }

    public List<Activity> listActivities(Long... context)
    {
        checkContext(context);
        return dao.listUserPublicActivity(context[0], filterService.present());
    }

    public List<Activity> listActivitiesForFeed(Long... context)
    {
        checkContext(context);
        return dao.listUserPublicActivity(
                context[0],
                filterService.present(),
                filterService.createAfter(DateUtils.oneMonthAgo()));
    }

    public List<Activity> listActivitiesRange(int startIdx, int range, Long... context)
    {
        checkContext(context);
        return dao.listUserPublicActivity(
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
                "UserActivitySource lookup methods require the user id as first and single parameter"); }
    }

}
