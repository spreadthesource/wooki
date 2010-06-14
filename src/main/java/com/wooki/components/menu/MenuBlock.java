package com.wooki.components.menu;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.links.Link;

public class MenuBlock
{
    @Parameter(defaultPrefix = BindingConstants.LITERAL, name = "class")
    @Property
    private String blockClasses;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, name = "id")
    @Property
    private String blockId;

    @Parameter
    @Property
    private List<Link> links;

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

    public String getRowClass()
    {
        if (index == listSize - 1) { return "last"; }
        return null;
    }

}
