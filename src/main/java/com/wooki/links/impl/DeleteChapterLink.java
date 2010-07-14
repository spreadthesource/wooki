package com.wooki.links.impl;

import com.wooki.domain.model.Chapter;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This link can be used to display a delete action link on a WoookiEntity resource.
 * 
 * @author ccordenier
 */
public class DeleteChapterLink extends AbstractEventLink
{
    private Chapter resource;

    public DeleteChapterLink(Chapter resource, String labelMessageKey, Object... context)
    {
        super("delete", labelMessageKey, "confirm-delete", context);
        this.resource = resource;
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return resource == null ? false : securityContext.canWrite(resource.getBook());
    }
}
