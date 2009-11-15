package com.wooki.domain.dao;

import com.wooki.domain.model.Author;

/**
 * Implements handling of Wooki Authors.
 * 
 * @author ccordenier
 * 
 */
public interface AuthorDAO {

	/**
	 * Add a new Author. Call this method before attaching a book to an author.
	 * 
	 * @param author
	 * @return
	 */
	Author add(Author author);

	/**
	 * Verify the password set is correct.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	boolean checkPassword(String username, String password);

	/**
	 * Find an author by its username, case is insensitive.
	 * 
	 * @param username
	 * @return
	 */
	Author findByUsername(String username);
	
}
