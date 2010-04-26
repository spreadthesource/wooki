package com.wooki.domain.dao;

import org.hibernate.Session;

import com.wooki.domain.model.Authority;

public class AuthorityDAOImpl extends WookiGenericDAOImpl<Authority, Long> implements AuthorityDAO
{

    public AuthorityDAOImpl(Session session)
    {
        super(session);
    }
    
}
