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
 * @author ccordenier
 * 
 * @param <T>
 * @param <PK>
 */
public class GenericDAOImpl<T extends WookiEntity, PK extends Serializable> implements
		GenericDAO<T, PK> {

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> entityType;

	public GenericDAOImpl() {
		this.entityType = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
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
		return entityManager.find(entityType, id);
	}

	public List<T> listAll() {
		Query query = this.entityManager.createQuery("from "
				+ this.getEntityType());
		List<T> resultList = (List<T>) query.getResultList();
		return resultList;
	}

	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	public String getEntityType() {
		return entityType.getName();
	}

}
