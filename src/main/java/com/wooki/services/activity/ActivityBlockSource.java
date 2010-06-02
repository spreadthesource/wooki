package com.wooki.services.activity;

import org.apache.tapestry5.Block;

import com.wooki.domain.model.activity.Activity;

/**
 * This services allows to contribute a list of block that will be used to display activities.
 *
 * @author ccordenier
 */
public interface ActivityBlockSource
{

    /**
     * Get a block in function of the activity's type. The block to use will be identified using the
     * type of passed activity instance. The naming convention is to use the simple name of the
     * Activity and to suffix it with 'Block' to build the name of the block to retrieve.
     *
     * @return
     */
    Block getActivityBlock(Activity activity);

}
