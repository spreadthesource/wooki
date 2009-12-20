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

package com.wooki.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * Integrate wymeditor as a mixin to be used with textarea.
 */
@IncludeJavaScriptLibrary("context:/static/js/wymeditor/jquery.wymeditor.pack.js")
public class WymEditor {

	@Parameter(defaultPrefix = BindingConstants.ASSET)
	private String wymStyle;

	@InjectContainer
	private TextArea container;

	@Inject
	private RenderSupport renderSupport;

	@AfterRender
	public void attachWymEditor() {

		
		JSONObject data = new JSONObject();
		data.put("elt", container.getClientId());
		
		JSONObject params = new JSONObject();
		params.put("logoHtml", "");

		if (wymStyle != null) {
			params.put("stylesheet", wymStyle);
		}

		data.put("params", params);
		
		// Use wymeditor
		renderSupport.addInit("initWymEdit", data);

	}
}
