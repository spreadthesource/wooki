package com.wooki.pages.dev;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.MoreEventResult;
import com.wooki.WookiEventConstants;

/**
 * Used to test the more link component.
 * 
 * @author ccordenier
 */
public class MoreLinkTest
{

    @Inject
    private Block display;

    @Property
    private List<Integer> values;

    @Property
    private int current;

    @OnEvent(value = WookiEventConstants.UPDATE_MORE_CONTEXT)
    public MoreEventResult getResult(int page)
    {
        MoreEventResult result = new MoreEventResult();
        if (page > 3)
        {
            result.setHasMore(false);
            return result;
        }
        this.setupValues(page);
        result.setHasMore(true);
        result.setRenderable(display);
        return result;
    }

    /**
     * Setup the values to display.
     * 
     * @param page
     */
    private void setupValues(int page)
    {
        values = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++)
        {
            values.add(i + (page * 10));
        }
    }
}
