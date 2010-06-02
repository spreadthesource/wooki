package com.wooki.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.UpdateListener;

/**
 * This service is responsible to internationalize messages used by Services.
 * 
 * @author ccordenier
 */
public interface ServicesMessages extends UpdateListener
{

    /**
     * Get the Messages used for services.
     * 
     * @return
     */
    Messages getMessages();

}
