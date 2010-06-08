package com.wooki.services.db;

/**
 * This interface defines a generic filter API used by the business layer to add query specifics
 * arguments. This will avoid too much multiplicity in the Data Layer only to specify extra
 * parameters to the query like a range...
 * 
 * @author ccordenier
 * @param <T>
 *            The type of Query, will depend on the implementation used.
 */
public interface QueryFilter<T>
{
    /**
     * This method will apply a filter on the current query.
     * 
     * @param query
     */
    void filter(T query);
}
