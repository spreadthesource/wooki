//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.wooki.domain.model.User;

public class UserDAOImpl extends WookiGenericDAOImpl<User, Long> implements UserDAO
{

    public UserDAOImpl(Session session)
    {
        super(session);
    }

    public User findByUsername(String username)
    {
        assert username != null;

        Query query = this.session.createQuery("from " + this.getEntityType()
                + " u where lower(u.username)=:un");
        query.setParameter("un", username.toLowerCase());
        try
        {
            return (User) query.uniqueResult();
        }
        catch (RuntimeException re)
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public String[] listUserNames(String prefix)
    {
        assert prefix != null;

        Query query = this.session.createQuery("select u.username from " + this.getEntityType()
                + " u where lower(u.username) like :un");
        query.setParameter("un", prefix.toLowerCase() + "%");
        List<String> result = (List<String>) query.list();
        if (result != null) { return result.toArray(new String[0]); }
        return new String[] {};
    }

    public BigInteger findSid(String username)
    {
        // selectSidPrimaryKey
        Query findSid = this.session
                .createSQLQuery("select id from acl_sid where principal=? and sid=?");
        findSid.setParameter(0, true);
        findSid.setParameter(1, username);
        return (BigInteger) findSid.uniqueResult();
    }

    public void updateSid(BigInteger sidId, String username)
    {
        Query updateSid = this.session.createSQLQuery("update acl_sid set sid=? where id=?");
        updateSid.setParameter(0, username);
        updateSid.setParameter(1, sidId);
        updateSid.executeUpdate();
    }

}
