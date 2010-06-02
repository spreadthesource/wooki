package com.wooki.services.activity.impl;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentSource;

import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivityBlockSource;

/**
 * This services implements simply lookup the block in a page dedicated to block instantiation. The
 * lookup strategy simply get the activity type simplename and try to find a block suffixed by
 * 'Block'.
 * 
 * @author ccordenier
 */
public class ActivityBlockSourceImpl implements ActivityBlockSource
{

    private static final String BLOCK_SUFFIX = "Block";

    private static final String ACTIVITY_BLOCK_PAGE = "activityDisplayBlocks";

    private final ComponentSource source;

    public ActivityBlockSourceImpl(ComponentSource source)
    {
        super();
        this.source = source;
    }

    public Block getActivityBlock(Activity activity)
    {
        Defense.notNull(activity, "Activity cannot be null");

        Component page = this.source.getPage(ACTIVITY_BLOCK_PAGE);
        ComponentResources resources = page.getComponentResources();

        Block result = resources.findBlock(activity.getClass().getSimpleName() + BLOCK_SUFFIX);

        if (result != null) { return result; }

        return resources.findBlock("empty");
    }

}
