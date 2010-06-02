package com.wooki.services.activity;

import com.wooki.domain.model.activity.Activity;

/**
 * Context class used to store datas that will be displayed by the corresponding activity block. An
 * object should be pushed into the environment before call the {@link ActivityBlockSource} service.
 * 
 * @author ccordenier
 */
public interface ActivityDisplayContext
{

    /**
     * Check if the resource targeted by the activity to display is still available and has not beed
     * deleted.
     * 
     * @return true if the target resource has been deleted.
     */
    boolean isResourceUnavailable();

    /**
     * Return the current activity.
     * 
     * @return
     */
    Activity getActivity();

    /**
     * @return The CSS style associated to the current row.
     */
    String getStyle();

}
