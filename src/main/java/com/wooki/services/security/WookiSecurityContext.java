package com.wooki.services.security;

import com.wooki.domain.model.User;

/**
 * Contains authorization and security behavior of wooki.
 *
 * @author ccordenier
 *
 */
public interface WookiSecurityContext {

	void log(User user);
	
	/**
	 * Check if a user is logged in.
	 *
	 * @return
	 */
	boolean isLoggedIn();
	
	/**
	 * Get the name of the logged user.
	 *
	 * @return
	 */
	User getAuthor();
	
	/**
	 * Check if the logged in user is author of a book.
	 *
	 * @param bookId
	 * @return
	 */
	boolean isAuthorOfBook(Long bookId);
	
	/**
	 * Check if the user is author of a comment
	 */
	boolean isAuthorOfComment(Long commentId);
	
}
