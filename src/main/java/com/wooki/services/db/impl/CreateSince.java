package com.wooki.services.db.impl;

import java.util.Date;

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.wooki.services.db.QueryFilter;


public class CreateSince implements QueryFilter<Criteria>
{

    private final Date date;

    public CreateSince(Date date)
    {
        Defense.notNull(date, "Requested date cannot be null");
        this.date = date;
    }

    public void filter(Criteria query)
    {
        query.add(Restrictions.gt("creationDate", this.date));
    }

}
