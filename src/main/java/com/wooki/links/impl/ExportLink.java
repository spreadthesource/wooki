package com.wooki.links.impl;

import com.wooki.services.security.WookiSecurityContext;

/**
 * This class is used to define a link that will export data i.e. PDF, RSS...
 * 
 * @author ccordenier
 */
public class ExportLink extends AbstractEventLink
{

    public ExportLink(String labelMessageKey, String printType, Object... context)
    {
        super(printType, labelMessageKey, context);
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return true;
    }

}
