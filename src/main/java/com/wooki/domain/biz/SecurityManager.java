package com.wooki.domain.biz;

import com.wooki.domain.model.Authority;
import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;

/**
 * Handle security concerns of wooki application. This service is in charge of affecting
 */
public interface SecurityManager
{
    /**
     * Set Owner Permission on a wooki entity.
     *
     * @param entity
     */
    void setOwnerPermission(WookiEntity entity);

    /**
     * Set author collaboration rights that mean a collaborator can r/w
     *
     * @param entity
     * @param user
     */
    void setCollaboratorPermission(WookiEntity entity, User user);
    
    /**
     * Check if the provided authority exists, create and store a new one if needed.
     *
     * @param authority
     * @return
     */
    Authority getOrCreateAuthority(String authority);
}
