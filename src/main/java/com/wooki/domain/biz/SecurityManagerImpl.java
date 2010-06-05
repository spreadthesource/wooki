package com.wooki.domain.biz;

import java.util.Calendar;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;

import com.wooki.domain.dao.AuthorityDAO;
import com.wooki.domain.model.Authority;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;
import com.wooki.domain.model.WookiGrantedAuthority;

public class SecurityManagerImpl implements SecurityManager
{
    private AclManager aclManager;

    @Inject
    @Autowired
    private AuthorityDAO authorityDao;

    public void setCollaboratorPermission(WookiEntity entity, User user)
    {
        this.aclManager.addPermission(entity, new PrincipalSid(user.getUsername()), BasePermission.WRITE, Book.class);
    }

    public void setOwnerPermission(WookiEntity entity)
    {
        Sid adminSid = new GrantedAuthoritySid(WookiGrantedAuthority.ROLE_ADMIN);
        this.aclManager.addPermission(entity, adminSid, BasePermission.ADMINISTRATION, entity
                .getClass());
        this.aclManager.addPermission(entity, BasePermission.ADMINISTRATION, entity.getClass());
    }

    public void setOwnerPermission(WookiEntity entity, User user)
    {
        this.aclManager.addPermission(entity, new PrincipalSid(user.getUsername()), BasePermission.ADMINISTRATION, Book.class);
    }

    public synchronized Authority getOrCreateAuthority(String authority)
    {
        Criteria result = this.authorityDao.createColumnCriteria("authority", authority);
        Authority auth = (Authority) result.uniqueResult();
        if (auth == null)
        {
            auth = new Authority(authority);
            auth.setCreationDate(Calendar.getInstance().getTime());
        }
        return auth;
    }

    public AclManager getAclManager()
    {
        return aclManager;
    }

    public void setAclManager(AclManager aclManager)
    {
        this.aclManager = aclManager;
    }

}
