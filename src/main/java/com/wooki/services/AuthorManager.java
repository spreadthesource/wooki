package com.wooki.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.exception.AuthorAlreadyException;
import com.wooki.domain.model.Author;

/**
 * Simple User management interface.
 * 
 * TODO Replace by or link to more general security feature
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface AuthorManager {

	/**
	 * Add a new author to DB
	 * 
	 * @param author
	 * @return
	 * @throws AuthorAlreadyException
	 */
	@Transactional(readOnly = false)
	Author addAuthor(Author author) throws AuthorAlreadyException;

	/**
	 * Can be used to check user login.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	boolean checkPassword(String username, String password);

	/**
	 * Verify that a user exists.
	 * 
	 * @param username
	 * @return
	 */
	Author findByUsername(String username);

}
