package com.wooki.components;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.base.WookiBase;
import com.wooki.pages.Index;
import com.wooki.services.SecurityUrlSource;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@Inject
	private RenderSupport support;

	@Inject
	private SecurityUrlSource source;

	@Inject
	private PageRenderLinkSource linkSource;

	@Property
	private String loginUrl;

	@Property
	private String logoutUrl;

	@SetupRender
	private void setup() {
		this.loginUrl = source.getLoginUrl();
		this.logoutUrl = source.getLogoutUrl();
	}

	@AfterRender
	public void initLoginDialog() {
		if (!isLogged()) {
			support.addInit("initLoginDialog");
		}
	}

	/**
	 * Return to page index without context.
	 * 
	 * @return
	 */
	public Link getIndexPage() {
		return linkSource.createPageRenderLinkWithContext(Index.class,
				new Object[0]);
	}

}
