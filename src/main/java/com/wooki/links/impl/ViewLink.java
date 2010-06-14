package com.wooki.links.impl;

import com.wooki.domain.model.WookiEntity;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Generak view link.
 *
 * @author ccordenier
 *
 */
public class ViewLink extends AbstractPageLink
{

    private boolean secured;

    private WookiEntity resource;

    public ViewLink(String page, String labelMessageKey, Object... context)
    {
        this(null, page, labelMessageKey, false, context);
    }

    public ViewLink(WookiEntity resource, String page, String labelMessageKey, boolean secured,
            Object... context)
    {
        super(page, labelMessageKey, context);
        this.resource = resource;
        this.secured = secured;
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return resource != null && secured ? securityContext.canWrite(resource) : true;
    }

}
