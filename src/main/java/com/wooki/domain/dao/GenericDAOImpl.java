//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.wooki.domain.model.WookiEntity;

/**
 * This generic implementation of DAO will be the base for all wooki's DAO.
 * 
 * @param <T>
 * @param <PK>
 */
public abstract class GenericDAOImpl<T extends WookiEntity, PK extends Serializable> implements
        GenericDAO<T, PK>
{

    protected Session session;

    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl(Session session)
    {
        this.session = session;

        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public T create(T o)
    {
        this.session.persist(o);

        return o;
    }

    @SuppressWarnings("unchecked")
    public T update(T o)
    {
        final T result = (T) this.session.merge(o);
        return result;
    }

    public void delete(T o)
    {
        o.setDeletionDate(new Date());
        this.session.merge(o);
    }

    @SuppressWarnings("unchecked")
    public T findById(PK id)
    {
        Defense.notNull(id, "id");

        Criteria crit = session.createCriteria(getEntityType());
        crit.add(Restrictions.idEq(id));

        return (T) crit.uniqueResult();
    }

    public String getEntityType()
    {
        return entityType.getName();
    }

    protected void setMaxResults(Query query, int max)
    {
        if (max > 0)
        {
            query.setMaxResults(max);
        }
    }

    public Criteria createCriteria()
    {
        return session.createCriteria(getEntityType());
    }

    public Criteria createCriteria(String alias)
    {
        return session.createCriteria(getEntityType(), alias);
    }
    
    public Criteria createColumnCriteria(String col, Object value) {
        Criteria crit = this.session.createCriteria(getEntityType());
        crit.add(Restrictions.eq(col, value));
        return crit;
    }

    @SuppressWarnings("unchecked")
    public List<T> list()
    {
        Criteria crit = session.createCriteria(getEntityType());
        return (List<T>) crit.list();
    }

}
