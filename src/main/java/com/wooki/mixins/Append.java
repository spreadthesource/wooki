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

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.InternalComponentResources;
import org.apache.tapestry5.internal.services.PartialMarkupDocumentLinker;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Request;

/**
 * Append content instead of replacing it.
 * 
 * @author ccordenier
 * 
 */
@MixinAfter
public class Append {

	/** Insert Position */
	@Parameter(value = "bottom", defaultPrefix = BindingConstants.LITERAL)
	private String position;

	/** Client id of element to append result */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String to;

	@Inject
	private AssetSource assetSource;

	@Inject
	private RenderSupport support;

	@Inject
	private Request request;

	@InjectContainer
	private Form form;

	/**
	 * Submit the form via Ajax and handle result to append instead of replacing
	 * the whole content.
	 * 
	 */
	@AfterRender
	public void append() {
		ComponentResources formResources = Component.class.cast(form).getComponentResources();
		if (formResources.isBound("zone")) {
			throw new IllegalStateException("'Append' mixin cannot be used if 'zone' parameter is set on form");
		}
		Object[] context = (Object[]) InternalComponentResources.class.cast(formResources).getParameterAccess("context").read(new Object[] {}.getClass());
		Link link = formResources.createFormEventLink(EventConstants.ACTION, context);
		support.addInit("appendToZone", link.toAbsoluteURI(), form.getClientId(), to, position);
	}

	/**
	 * Generate an errors message that will appear on the client side.
	 * 
	 * @return
	 */
	@OnEvent(value = EventConstants.FAILURE)
	public Object checkErrors() {

		PartialMarkupDocumentLinker linker = new PartialMarkupDocumentLinker();
		linker.addStylesheetLink(assetSource.getContextAsset("context:/static/css/jquery.notifyBar.css", request.getLocale()).toClientURL(), null);
		linker.addScriptLink(assetSource.getContextAsset("/static/js/jquery.notifyBar.js", request.getLocale()).toClientURL());
		linker.addScriptLink(assetSource.getContextAsset("context:/static/js/notifybar.js", request.getLocale()).toClientURL());
		linker.addScriptLink(assetSource.getContextAsset("/static/js/notifybar.js", request.getLocale()).toClientURL());

		JSONObject result = new JSONObject();
		StringBuffer buff = new StringBuffer();
		List<String> errors = form.getDefaultTracker().getErrors();
		if (!errors.isEmpty()) {
			buff.append("<div class=\"error-list shadowed\">");
			buff.append("<ul class=\"error-list wrapper\">");
			for (String error : errors) {
				buff.append("<li>").append(error).append("</li>");
			}
			buff.append("</ul>");
			buff.append("</div>");
			result.put("errors", true);
		} else {
			buff.append("Unexcepted error");
			result.put("errors", true);
		}

		// Add error messages
		JSONObject html = new JSONObject();
		html.put("html", buff.toString());
		linker.addScript(String.format("Tapestry.Initializer.initErrorMessage(%s);", html.toString()));

		linker.commit(result);
		return result;
	}

}
