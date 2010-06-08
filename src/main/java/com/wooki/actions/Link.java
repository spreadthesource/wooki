package com.wooki.actions;

import com.wooki.services.security.WookiSecurityContext;

/**
 * 
 * @author ccordenier
 */
public interface Link
{

    /**
     * Return true if the logged user has enough rights to access to this action.
     * 
     * @return
     */
    boolean isAuthorized(WookiSecurityContext securityContext);

    /**
     * Return the context used to generate the link.
     * 
     * @return
     */
    Object[] getContext();

    /**
     * Return the label to display with the link
     * 
     * @return
     */
    String getLabelMessageKey();

}
