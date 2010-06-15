package com.wooki.links.impl;

import org.apache.tapestry5.ioc.MessageFormatter;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Navigation links are located on top of the page, they are used to navigate in book pages.
 * 
 * @author ccordenier
 */
public class NavLink extends AbstractPageLink
{
    private String title;

    public NavLink(String page, String labelKey, String title, Object... context)
    {
        this(page, labelKey, null, title, context);
    }

    public NavLink(String page, String labelKey, String confirmMessageKey, String title,
            Object... context)
    {
        super(page, labelKey, confirmMessageKey, context);
        this.title = title;
    }

    public boolean isAuthorized(WookiSecurityContext securityContext)
    {
        return true;
    }

    @Override
    public String format(MessageFormatter formatter)
    {
        return formatter.format(title);
    }

}
