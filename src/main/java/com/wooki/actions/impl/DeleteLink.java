package com.wooki.actions.impl;

import com.wooki.domain.model.WookiEntity;
import com.wooki.services.security.WookiSecurityContext;

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
