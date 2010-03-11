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

package com.wooki.pages.dev;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;

@IncludeJavaScriptLibrary({ "context:/static/js/jquery.notifyBar.js", "context:/static/js/notifybar.js" })
@IncludeStylesheet("context:/static/css/jquery.notifyBar.css")
public class Full {

	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	private boolean productionMode;

	@Inject
	private RenderSupport renderSupport;

	@OnEvent(value = EventConstants.ACTIVATE)
	private Object redirect() {
		if (productionMode)
			return com.wooki.pages.Index.class;

		return null;
	}

	@AfterRender
	public void initJs() {
		renderSupport.addInit("initErrorBox", "error-list");
		JSONObject data = new JSONObject();
		data.put("showLnkId", "add-chapter");
		data.put("toShow", "add-chapter-form");
		data.put("hideLnkId", "hide-add-chapter");
		data.put("duration", "200");
		renderSupport.addInit("initShowHideEffect", data);
	}
}
