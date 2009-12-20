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
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

/**
 * Open the link in a new window.
 *
 * @author ccordenier
 *
 */
@MixinAfter
public class OpenInWindow {

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value="componentResources.id")
	private String name;

	@Parameter(defaultPrefix= BindingConstants.LITERAL, value="directories=no,location=no,menubar=no,resizable=yes,toolbar=no")
	private String options;
	
	@Inject
	private RenderSupport renderSupport;
	
	@InjectContainer
	private AbstractLink link;
	
	@AfterRender
	public void initLink() {
		JSONObject param = new JSONObject();
		param.put("elt", link.getClientId());
		param.put("url", link.getLink().toAbsoluteURI());
		param.put("name", name);
		param.put("options", options);
		renderSupport.addInit("initOpenInWindow", param);
	}
	
}
