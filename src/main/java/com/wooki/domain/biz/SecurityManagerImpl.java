package com.wooki.domain.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;
import com.wooki.domain.model.WookiGrantedAuthority;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@Component("securityManager")
public class SecurityManagerImpl implements SecurityManager
{
    @Autowired
    private AclManager aclManager;

    public void setCollaboratorPermission(WookiEntity entity, User user)
    {
        Sid sid = new PrincipalSid(user.getUsername());
        this.aclManager.addPermission(entity, BasePermission.WRITE, Book.class);
    }

    @Transactional(readOnly = false)
    public void setOwnerPermission(WookiEntity entity)
    {
        Sid adminSid = new GrantedAuthoritySid(WookiGrantedAuthority.ROLE_ADMIN);
        this.aclManager.addPermission(entity, adminSid, BasePermission.ADMINISTRATION, entity.getClass());
        this.aclManager.addPermission(entity, BasePermission.ADMINISTRATION, entity.getClass());
    }

}
