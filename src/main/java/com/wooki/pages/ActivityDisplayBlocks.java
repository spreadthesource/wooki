package com.wooki.pages;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.activity.Activity;
import com.wooki.services.ServicesMessages;
import com.wooki.services.activity.ActivityDisplayContext;

/**
 * This page simply holds all the blocks used to display activity items.
 *
 * @author ccordenier
 *
 */
public class ActivityDisplayBlocks
{

    @Environmental
    private ActivityDisplayContext context;

    @Inject
    private ServicesMessages messages;

    public String getActivityLabel(String type)
    {
        return messages.getMessages().get(type);
    }
    
    public String getStyle()
    {
        return this.context.getStyle();
    }

    public Activity getActivity()
    {
        return this.context.getActivity();
    }

    public boolean isResourceUnavailable()
    {
        return this.context.isResourceUnavailable();
    }

}
