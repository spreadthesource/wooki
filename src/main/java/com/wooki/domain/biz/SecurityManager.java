package com.wooki.domain.biz;

import com.wooki.domain.model.Book;

/**
 * Handle security concerns of wooki application. This service is in charge of
 * affecting
 * 
 * @author ccordenier
 * 
 */
public interface SecurityManager {

	void setOwnerPermission(Book book);

	void setCollaboratorPermission(Book book);
}
