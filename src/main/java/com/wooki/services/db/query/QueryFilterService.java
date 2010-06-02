package com.wooki.services.db.query;

import java.util.Date;


/**
 * The service that is used to create wooki application's specific query filter must implement this
 * interface. Default implementation uses Hibernate Criteria API.
 * 
 * @author ccordenier
 */
public interface QueryFilterService
{

    @SuppressWarnings("unchecked")
    QueryFilter range(int startIdx, int range);

    @SuppressWarnings("unchecked")
    QueryFilter createAfter(Date date);

    @SuppressWarnings("unchecked")
    QueryFilter present();

}
