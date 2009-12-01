package com.wooki.domain.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO will be the base for all concrete DAO implementation.
 * It will provide generic data access method. 
 *
 * @author ccordenier
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericDAO<T, PK extends Serializable> {

	/** Persist the newInstance object into database */
	void create(T newInstance);

	/**
	 * Retrieve an object that was previously persisted to the database using
	 * the indicated id as primary key
	 */
	T findById(PK id);

	/** Save changes made to a persistent object. 
	 * @return TODO*/
	T update(T transientObject);

	/** Remove an object from persistent storage in the database */
	void delete(T persistentObject);

	/** List all the persistent entities of type T */
	List<T> listAll();
}
