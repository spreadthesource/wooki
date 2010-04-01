package com.wooki.domain.biz;

import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;

/**
 * Handle security concerns of wooki application. This service is in charge of affecting
 * 
 * @author ccordenier
 */
public interface SecurityManager
{
    /**
     * Set Owner Permission on a wooki entity.
     *
     * @param entity
     */
    void setOwnerPermission(WookiEntity entity);

    void setCollaboratorPermission(WookiEntity entity, User user);
}
