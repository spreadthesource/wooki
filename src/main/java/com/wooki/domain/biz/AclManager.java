package com.wooki.domain.biz;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import com.wooki.domain.model.WookiEntity;

/**
 * Manage permissions on the different entities.
 */
public interface AclManager
{
    public void addPermission(WookiEntity securedObject, Permission permission, Class<?> clazz);
    
    public void addPermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz);

    public void deletePermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz);

}
