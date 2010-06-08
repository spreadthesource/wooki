package com.wooki.actions.impl;

import com.wooki.domain.model.WookiEntity;
import com.wooki.services.security.WookiSecurityContext;

public class EditLink extends AbstractPageLink
{
    private WookiEntity resource;

    public EditLink(WookiEntity resource, String page, String labelMessageKey, Object... context)
    {
        super(page, labelMessageKey, context);
        this.resource = resource;
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return securityContext.canWrite(resource);
    }
}
