package com.wooki.components;

import nu.localhost.tapestry5.springsecurity.services.LogoutService;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;

import com.wooki.base.WookiBase;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private LogoutService logoutService;

	@Inject
	private RenderSupport support;
	
	@Inject
	private AssetSource source;
	
	@Inject
	private ThreadLocale locale;

	/**
	 * Simply invalidate the session and return to signin page.
	 * 
	 * @return
	 */
	@OnEvent(component = "logout")
	public void logout() {
		logoutService.logout();
	}

	@AfterRender
	public void initLoginDialog() {
		if (!isLogged()) {
			Asset jQueryUI = source.getContextAsset("static/js/jquery-ui-1.7.2.custom.min.js", locale.getLocale());
			support.addScriptLink(jQueryUI);
			
			JSONObject data = new JSONObject();
			data.put("elt", "login-dialog-form");
			
			JSONObject params = new JSONObject();
			params.put("modal", true);
			params.put("width", 300);
			params.put("height", 200);
			data.put("params", params);
			
			System.out.println(data);
			
			//this is WIP
			//support.addInit("initLoginDialog", data);
		}
	}

}
