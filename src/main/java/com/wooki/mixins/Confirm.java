package com.wooki.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * Add confirm dialog box.
 * 
 * @author ccordenier
 */
@MixinAfter
public class Confirm
{

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String message;

    @Inject
    private RenderSupport support;

    @InjectContainer
    private AbstractLink lnk;

    @AfterRender
    public void addConfirm()
    {
        JSONObject params = new JSONObject();
        if (message != null)
        {
            params.put("message", message);
        }
        params.put("lnkId", lnk.getClientId());
        support.addInit("initConfirm", params);
    }

}
