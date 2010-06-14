package com.wooki.links;

/**
 * Defines an action event in wooki.
 * 
 * @author ccordenier
 */
public interface EventLink extends Link
{

    /**
     * Get the event type.
     *
     * @return
     */
    public String getEvent();

}
