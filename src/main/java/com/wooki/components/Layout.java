//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.components;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.base.WookiBase;
import com.wooki.pages.Index;
import com.wooki.pages.SearchResult;
import com.wooki.services.SecurityUrlSource;

@IncludeStylesheet("context:static/css/style.css")
public class Layout extends WookiBase {

	@InjectPage
	private SearchResult searchResult;
	
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

	@Property
	private String queryString;
	
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

	@OnEvent(value = EventConstants.SUCCESS, component="searchForm")
	private Object search() {
		searchResult.search(queryString);
		return searchResult;
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
