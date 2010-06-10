package com.wooki.actions.impl;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Print book link used to generate a PDF from a book.
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
