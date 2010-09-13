package com.wooki.links.impl;

import com.wooki.links.PageLink;

/**
 * Base class for action links.
 * 
 * @author ccordenier
 * @param <T>
 */
public abstract class AbstractPageLink extends AbstractLink implements PageLink
{

    private String page;

    private boolean selected;

    public AbstractPageLink(String page, String labelKey, Object... context)
    {
        super(labelKey, context);
        this.page = page;
    }

    public AbstractPageLink(String page, String labelKey, String confirmMessageKey,
            Object... context)
    {
        super(labelKey, confirmMessageKey, context);
        this.page = page;
    }

    public AbstractPageLink(String page, String labelKey, String confirmMessageKey,
            boolean selected, Object... context)
    {
        super(labelKey, confirmMessageKey, context);
        this.page = page;
        this.selected = selected;
    }

    public String getPage()
    {
        return page;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

}
