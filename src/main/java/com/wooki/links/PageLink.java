package com.wooki.links;

/**
 * Defines a link dedicated to page.
 * 
 * @author ccordenier
 */
public interface PageLink extends Link
{

    /**
     * Returns the page targeted by the link.
     * 
     * @return
     */
    String getPage();
}
