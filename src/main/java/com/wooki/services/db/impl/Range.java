package com.wooki.services.db.impl;

import org.hibernate.Criteria;

import com.wooki.services.db.QueryFilter;


public class Range implements QueryFilter<Criteria>
{

    private final int startIdx;

    private final int range;

    public Range(int startIdx, int range)
    {
        super();
        this.startIdx = startIdx;
        this.range = range;
    }

    public void filter(Criteria query)
    {
        query.setFirstResult(this.startIdx);
        query.setMaxResults(this.range);
    }

}
