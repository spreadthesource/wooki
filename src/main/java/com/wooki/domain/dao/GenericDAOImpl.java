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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.wooki.domain.model.WookiEntity;

/**
 * This generic implementation of DAO will be the base for all wooki's DAO.
 * 
 * @param <T>
 * @param <PK>
 */
public class GenericDAOImpl<T extends WookiEntity, PK extends Serializable> implements GenericDAO<T, PK> {

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> entityType;

	public GenericDAOImpl() {
		this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void create(T o) {
		this.entityManager.persist(o);
	}

	public T update(T o) {
		final T result = this.entityManager.merge(o);
		return result;
	}

	public void delete(T o) {
		o.setDeletionDate(new Date());
		this.entityManager.merge(o);
	}

	public T findById(PK id) {
		if (id == null)
			throw new IllegalArgumentException("Id for " + entityType.getCanonicalName() + " cannot be null.");
		Query query = entityManager.createQuery("from " + this.getEntityType() + " e where e.deletionDate is null and e.id=:id");
		query.setParameter("id", id);
		List<T> results = (List<T>) query.getResultList();
		if (results != null && results.size() == 1) {
			return results.get(0);
		}
		return null;
	}

	public List<T> listAll() {
		Query query = this.entityManager.createQuery("from " + this.getEntityType());
		List<T> resultList = (List<T>) query.getResultList();
		return resultList;
	}

	public String getEntityType() {
		return entityType.getName();
	}

	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	protected void setMaxResults(Query query, int max) {
		if (max > 0) {
			query.setMaxResults(max);
		}
	}

}
