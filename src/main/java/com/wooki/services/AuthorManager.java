package com.wooki.services;

import com.wooki.domain.exception.AuthorAlreadyException;
import com.wooki.domain.model.Author;

/**
 * Author registration and retrival manager.
 * 
 */
public interface AuthorManager {

	/**
	 * Add a new author to DB
	 * 
	 * @param author
	 * @return
	 * @throws AuthorAlreadyException
	 */
	void addAuthor(Author author) throws AuthorAlreadyException;

	/**
	 * Verify that a user exists.
	 * 
	 * @param username
	 * @return
	 */
	Author findByUsername(String username);

}
