package com.wooki.components;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.RequestGlobals;

/**
 * Add a link in your HTML header section to refer to the Feed corresponding to the current page.
 * 
 * @author ccordenier
 */
public class FeedLink extends AbstractLink
{

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "feed")
    private String event;

    /**
     * The logical name of the page to link to.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String page;

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String titleKey;

    @Parameter
    private Object[] titleFormat;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "http")
    private String protocol;

    /**
     * If provided, this is the activation context for the target page (the information will be
     * encoded into the URL). If not provided, then the target page will provide its own activation
     * context.
     */
    @Parameter
    private Object[] context;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources resources;

    @Inject
    private ComponentSource source;

    @Inject
    private RequestGlobals request;

    String defaultEvent()
    {
        return resources.getId();
    }

    @AfterRenderTemplate
    void createLink(MarkupWriter writer)
    {
        ComponentResources containerResources = resources.isBound("page") ? source.getComponent(
                page).getComponentResources() : resources.getContainerResources();
        Link link = containerResources.createEventLink(event, context);
        String title = resources.isBound("titleFormat") ? this.messages.format(
                titleKey,
                titleFormat) : this.messages.get(titleKey);

        // Use to build a full URL
        HttpServletRequest httpRequest = request.getHTTPServletRequest();

        // Add link to the header
        Element head = writer.getDocument().find("html/head");
        head.element(
                "link",
                "type",
                "application/atom+xml",
                "title",
                title,
                "rel",
                "alternate",
                "href",
                protocol + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort()
                        + link.toAbsoluteURI());
    }
}
