package com.wooki.components.menu;

import org.apache.tapestry5.annotations.Parameter;

import com.wooki.links.PageLink;

/**
 * This component display the navigation bar that will be displayed on top of the page.
 *
 * @author ccordenier
 *
 */
public class NavBar
{

    @Parameter
    private PageLink left;

    @Parameter
    private PageLink right;

    @Parameter
    private PageLink center;
    
}
