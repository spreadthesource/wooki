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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Make a link to update a zone and popup a dialog box
 */
public class AjaxDialogLink extends DialogLink {

	@Parameter(required = true)
	private String zone;

	@Parameter
	private Object[] context;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	@Override
	@AfterRender
	void declareDialog(MarkupWriter writer) {
		writer.end();

		Link link = resources.createEventLink(EventConstants.ACTION, context);

		support.addInit("openJQueryAjaxDialogOnClick", getClientId(), zone, getDialog(), link.toAbsoluteURI());
	}
}
