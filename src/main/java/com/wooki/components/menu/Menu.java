package com.wooki.components.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.links.Link;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This component is in charge of displaying the menu bar on wooki's pages.
 * 
 * @author ccordenier
 */
public class Menu
{

    @Parameter(autoconnect = true)
    private List<Link> adminLinks;

    @Parameter(autoconnect = true)
    private List<Link> publicLinks;

    @Inject
    private WookiSecurityContext securityContext;

    @Property
    private List<Link> availableAdminLinks;

    @Property
    private List<Link> availablePublicLinks;

    /**
     * Check which links should be displayed in the menu bar, in function of the current security
     * context.
     */
    @SetupRender
    void setupAvailableLinks()
    {
        if (adminLinks != null && adminLinks.size() > 0)
        {
            availableAdminLinks = new ArrayList<Link>();
            for (Link lnk : adminLinks)
            {
                if (lnk.isAuthorized(securityContext))
                {
                    availableAdminLinks.add(lnk);
                }
            }
        }

        if (publicLinks != null && publicLinks.size() > 0)
        {
            availablePublicLinks = new ArrayList<Link>();
            for (Link lnk : publicLinks)
            {
                if (lnk.isAuthorized(securityContext))
                {
                    availablePublicLinks.add(lnk);
                }
            }
        }
    }

}
