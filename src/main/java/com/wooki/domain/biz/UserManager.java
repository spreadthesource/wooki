package com.wooki.domain.biz;

import java.util.List;

import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;

/**
 * Author registration and retrival manager.
 * 
 */
public interface UserManager {

	/**
	 * Add a new author to DB
	 * 
	 * @param user
	 * @return
	 * @throws UserAlreadyException
	 */
	void addUser(User user) throws UserAlreadyException;

	/**
	 * Verify that a user exists.
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * Get the list of user whos name starts with prefix.
	 *
	 * @param prefix
	 * @return
	 */
	String[] listUserNames(String prefix);
	
}
