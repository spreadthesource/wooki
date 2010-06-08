package com.wooki.services.db.impl;

import java.util.Date;

import com.wooki.services.db.QueryFilter;
import com.wooki.services.db.QueryFilterService;


/**
 * Only hibernate Criteria based implementation exists for now.
 * 
 * @author ccordenier
 */
public class QueryFilterServiceImpl implements QueryFilterService
{

    @SuppressWarnings("unchecked")
    public QueryFilter createAfter(Date date)
    {
        return new CreateSince(date);
    }

    @SuppressWarnings("unchecked")
    public QueryFilter range(int startIdx, int range)
    {
        return new Range(startIdx, range);
    }

    @SuppressWarnings("unchecked")
    public QueryFilter present()
    {
        return new Present();
    }

}
