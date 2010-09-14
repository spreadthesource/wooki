package com.wooki.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractComponentEventLink;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Same as {@link EventLink} but link is generated from root page and not current component.
 * 
 * @author ccordenier
 */
public class RootEventLink extends AbstractComponentEventLink
{
    /**
     * The name of the event to be triggered in the parent component. Defaults to the id of the
     * component. An {@link org.apache.tapestry5.corelib.components.ActionLink} triggers an "action"
     * event on itself, and EventLink component triggers any arbitrary event on
     * <em>its container</em>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String event;

    @Inject
    private ComponentResources resources;

    String defaultEvent()
    {
        return resources.getId();
    }

    @Override
    protected Link createLink(Object[] eventContext)
    {
        ComponentResources containerResources = resources.getPage().getComponentResources();

        return containerResources.createEventLink(event, eventContext);
    }
}