//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.base.LayoutBase;
import com.wooki.pages.Index;
import com.wooki.pages.SearchResult;
import com.wooki.services.SecurityUrlSource;
import com.wooki.services.security.WookiSecurityContext;

public class Layout extends LayoutBase {

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "wooki - Collaborative Writing")
	private String title;

	@Inject
	private WookiSecurityContext securitCtx;

	@Inject
	private RenderSupport support;

	@Inject
	private SecurityUrlSource source;

	@Inject
	private PageRenderLinkSource linkSource;

	@InjectPage
	private SearchResult searchResult;

	@Property
	private String loginUrl;

	@Property
	private String logoutUrl;

	@Validate("required")
	@Property
	private String queryString;

	@SetupRender
	private void setup() {
		this.loginUrl = source.getLoginUrl();
		this.logoutUrl = source.getLogoutUrl();
	}

	@AfterRender
	public void initLoginDialog() {
		if (!securitCtx.isLoggedIn()) {
			support.addInit("initLoginDialog");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "search")
	private Object search() {
		searchResult.search(queryString);
		return searchResult;
	}

	/**
	 * Return to page index without context.
	 */
	public Link getIndexPage() {
		return linkSource.createPageRenderLinkWithContext(Index.class, new Object[0]);
	}

}
