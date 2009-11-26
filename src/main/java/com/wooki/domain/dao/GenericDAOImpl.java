package com.wooki.domain.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

/**
 * This generic implementation of DAO will be the base for all wooki's DAO.
 * 
 * @author ccordenier
 * 
 * @param <T>
 * @param <PK>
 */
public class GenericDAOImpl<T, PK extends Serializable> implements
		GenericDAO<T, PK> {

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> type;

	public GenericDAOImpl() {
		this.type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public PK create(T o) {
		return (PK) getSession().save(o);
	}

	public void update(T o) {
		getSession().update(o);
	}

	public void delete(T o) {
		getSession().delete(o);
	}

	public T findById(PK id) {
		return (T) getSession().load(type, id);
	}

	public List<T> listAll() {
		return getSession().createCriteria(type).list();
	}

	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}

}
