package com.wooki.domain.biz;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.model.Sid;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;
import com.wooki.domain.model.WookiGrantedAuthority;

public class SecurityManagerImpl implements SecurityManager
{

    private AclManager aclManager;

    public void setCollaboratorPermission(WookiEntity entity, User user)
    {
        this.aclManager.addPermission(entity, BasePermission.WRITE, Book.class);
    }

    public void setOwnerPermission(WookiEntity entity)
    {
        Sid adminSid = new GrantedAuthoritySid(WookiGrantedAuthority.ROLE_ADMIN);
        this.aclManager.addPermission(entity, adminSid, BasePermission.ADMINISTRATION, entity
                .getClass());
        this.aclManager.addPermission(entity, BasePermission.ADMINISTRATION, entity.getClass());
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
