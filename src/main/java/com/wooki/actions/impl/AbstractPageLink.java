package com.wooki.actions.impl;

import com.wooki.actions.PageLink;

/**
 * Base class for action links.
 * 
 * @author ccordenier
 * @param <T>
 */
public abstract class AbstractPageLink extends AbstractLink implements PageLink
{

    private String page;

    public AbstractPageLink(String page, String labelKey, Object[] context)
    {
        super(labelKey, context);
        this.page = page;
    }

    public String getPage()
    {
        return page;
    }

}
