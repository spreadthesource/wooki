package com.wooki.links;

import org.apache.tapestry5.ioc.MessageFormatter;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Base interface for wooki's link.
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

    /**
     * Return the key of the message that will popup when the user click on the link.
     * 
     * @return
     */
    String getConfirmMessageKey();
    
    /**
     * This method is called to format the label that will be displayed to the user
     *
     * @param formatter
     * @return
     */
    String format(MessageFormatter formatter);

}
