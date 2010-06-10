package com.wooki.actions.impl;

import com.wooki.actions.EventLink;

/**
 * Base class for action links.
 * 
 * @author ccordenier
 * @param <T>
 */
public abstract class AbstractEventLink extends AbstractLink implements EventLink
{

    private String event;

    public AbstractEventLink(String labelKey, Object[] context)
    {
        this("action", labelKey, context);
    }

    public AbstractEventLink(String event, String labelKey, Object[] context)
    {
        super(labelKey, context);
        this.event = event;
    }

    public AbstractEventLink(String event, String labelKey, String confirmMessageKey,
            Object[] context)
    {
        super(labelKey, confirmMessageKey, context);
        this.event = event;
    }

    public String getEvent()
    {
        return event;
    }

}
