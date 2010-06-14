package com.wooki.links.impl;

import com.wooki.domain.model.WookiEntity;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This link can be used to display a delete action link on a WoookiEntity resource.
 * 
 * @author ccordenier
 *
 */
public class EditLink extends AbstractPageLink
{
    private WookiEntity resource;

    public EditLink(WookiEntity resource, String page, String labelMessageKey, Object... context)
    {
        super(page, labelMessageKey, context);
        this.resource = resource;
    }

    public EditLink(WookiEntity resource, String page, String labelMessageKey, String confirmMessageKey, Object... context)
    {
        super(page, labelMessageKey, confirmMessageKey, context);
        this.resource = resource;
    }
    
    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return securityContext.canWrite(resource);
    }
}
