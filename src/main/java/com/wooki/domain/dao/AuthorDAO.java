package com.wooki.domain.dao;

import com.wooki.domain.model.Author;

/**
 * Implements handling of Wooki Authors.
 * 
 * @author ccordenier
 * 
 */
public interface AuthorDAO extends GenericDAO<Author, Long>{

	/**
	 * Find an author by its username, case is insensitive.
	 * 
	 * @param username
	 * @return
	 */
	Author findByUsername(String username);
	
}
