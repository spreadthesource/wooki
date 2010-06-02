package com.wooki.services.db.query;

import org.hibernate.Criteria;


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
