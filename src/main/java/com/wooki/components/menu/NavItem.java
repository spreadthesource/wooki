package com.wooki.components.menu;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.wooki.links.PageLink;

/**
 * This component display the navigation bar that will be displayed on top of the page.
 * 
 * @author ccordenier
 */
public class NavItem implements ClientElement
{
    @Parameter
    @Property
    private PageLink link;

    @Parameter(name = "id", defaultPrefix = BindingConstants.LITERAL)
    private String idParameter;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @Inject
    private JavaScriptSupport jsSupport;

    @InjectComponent
    private org.apache.tapestry5.corelib.components.PageLink plink;

    private String clientId;

    void beginRender()
    {
        clientId = resources.isBound("id") ? idParameter : javascriptSupport
                .allocateClientId(resources);
    }

    @AfterRender
    void setupConfirm()
    {
        if (link != null && link.getConfirmMessageKey() != null)
        {
            JSONObject params = new JSONObject();
            params.put("lnkId", plink.getClientId());
            params.put("message", messages.get(link.getConfirmMessageKey()));
            jsSupport.addInitializerCall("initConfirm", params);
        }
    }

    public String getLabel()
    {
        MessageFormatter formatter = messages.getFormatter(link.getLabelMessageKey());
        return link.format(formatter);
    }

    public String getClientId()
    {
        return clientId;
    }

}
