package com.wooki.services.security;

import org.apache.tapestry5.EventContext;

/**
 * This service is used to make a strict checking of the activation context for a given request.
 * 
 * @author ccordenier
 */
public interface ActivationContextManager
{

    /**
     * This method is called by a front filter to do strict activation checking before executing the
     * request.
     * 
     * @param pageName
     * @param ctx
     * @return
     */
    boolean checkContext(String pageName, EventContext ctx);

}
