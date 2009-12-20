package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.User;

/**
 * Implements handling of Wooki Authors.
 * 
 * @author ccordenier
 * 
 */
public interface UserDAO extends GenericDAO<User, Long>{

	/**
	 * Find an author by its username, case is insensitive.
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
	
	/**
	 * List usernames
	 *
	 * @param prefix
	 * @return
	 */
	String[] listUserNames(String prefix);

}
