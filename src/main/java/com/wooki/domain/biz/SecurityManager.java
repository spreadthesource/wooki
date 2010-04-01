package com.wooki.domain.biz;

import com.wooki.domain.model.Book;

/**
 * Handle security concerns of wooki application. This service is in charge of affecting
 */
public interface SecurityManager
{

    void setOwnerPermission(Book book);

    void setCollaboratorPermission(Book book);
}
