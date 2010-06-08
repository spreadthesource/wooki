package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.actions.EventLink;
import com.wooki.actions.Link;
import com.wooki.actions.PageLink;

public class MenuBlock
{
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String blockClasses;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String blockId;

    @Parameter
    @Property
    private List<Link> links;

    @Inject
    private Messages messages;

    @Property
    private Link current;

    @Property
    private int index;

    @Property
    private int listSize;

    Object setupRender()
    {
        if (links != null)
        {
            listSize = links.size();
        }

        if (listSize == 0) { return false; }

        return true;
    }

    public boolean isAction()
    {
        return current instanceof EventLink;
    }

    public String getLabel()
    {
        return messages.get(current.getLabelMessageKey());
    }

    public EventLink getCurrentEventLink()
    {
        return EventLink.class.cast(current);
    }

    public PageLink getCurrentPageLink()
    {
        return PageLink.class.cast(current);
    }

    public String getRowClass()
    {
        if (index == listSize - 1) { return "last"; }
        return null;
    }

}
