package com.wooki.domain.biz;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import com.wooki.domain.model.WookiEntity;

/**
 * Manage permissions on the different entities.
 */
public interface AclManager
{

    @CommitAfter
    public void addPermission(WookiEntity securedObject, Permission permission, Class<?> clazz);

    @CommitAfter
    public void addPermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz);

    @CommitAfter
    public void deletePermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz);

}
