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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

public class DialogLink implements ClientElement {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	@Parameter(required = true)
	private String dialog;

	@Inject
	private RenderSupport support;

	@Inject
	private ComponentResources resources;

	@BeginRender
	void startDiv(MarkupWriter writer) {
		writer.element("a", "id", getClientId(), "href", "#");
	}

	@AfterRender
	void declareDialog(MarkupWriter writer) {
		writer.end();
		support.addInit("openJQueryDialogOnClick", getClientId(), dialog);
	}

	public String getClientId() {
		if (clientId == null) {
			clientId = support.allocateClientId(resources);
		}
		return this.clientId;
	}
	
	protected String getDialog() {
		return this.dialog;
	}
}
