package com.wooki.links.impl;

import com.wooki.domain.model.WookiEntity;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This link can be used to display a delete action link on a WoookiEntity resource.
 * 
 * @author ccordenier
 */
public class DeleteLink extends AbstractEventLink
{
    private WookiEntity resource;

    public DeleteLink(WookiEntity resource, String labelMessageKey, Object... context)
    {
        super("delete", labelMessageKey, "confirm-delete", context);
        this.resource = resource;
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return securityContext.canDelete(resource);
    }
}
