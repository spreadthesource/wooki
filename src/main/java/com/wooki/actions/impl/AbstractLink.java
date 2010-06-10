package com.wooki.actions.impl;

import com.wooki.actions.Link;

/**
 * Adapter class for link.
 * 
 * @author ccordenier
 * @param <T>
 */
public abstract class AbstractLink implements Link
{
    private String label;

    private Object[] context;

    private String confirmMessage;
    
    public AbstractLink(String labelMessageKey, Object... context)
    {
        super();
        this.label = labelMessageKey;
        this.context = context;
    }

    public AbstractLink(String labelMessageKey, String confirmMessageKey, Object... context)
    {
        super();
        this.label = labelMessageKey;
        this.confirmMessage = confirmMessageKey;
        this.context = context;
    }
    
    public Object[] getContext()
    {
        return context;
    }

    public String getLabelMessageKey()
    {
        return label;
    }

    public String getConfirmMessageKey()
    {
        return confirmMessage;
    }

}
