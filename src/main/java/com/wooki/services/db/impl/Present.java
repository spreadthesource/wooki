package com.wooki.services.db.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.wooki.services.db.QueryFilter;


/**
 * Defines the hibernate implementation of existing resource. This will be useful to get only the
 * entries that has not been deleted by the user.
 * 
 * @author ccordenier
 */
public class Present implements QueryFilter<Criteria>
{

    public void filter(Criteria query)
    {
        query.add(Restrictions.isNull("deletionDate"));
    }

}
