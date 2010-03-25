package com.wooki.base;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.ServicesMessages;

/**
 * Base class for activity components.
 * 
 * @author ccordenier
 */
public class AbstractActivity
{

    @Inject
    private ServicesMessages messages;

    @Parameter
    private boolean resourceAvailable;

    @Property
    @Parameter(value = "")
    private String style;

    public String getActivityLabel(String type)
    {
        return messages.getMessages().get(type);
    }

}
