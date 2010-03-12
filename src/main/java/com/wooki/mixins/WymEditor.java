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

import java.io.IOException;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.util.TextStreamResponse;

import com.wooki.WookiSymbolsConstants;
import com.wooki.services.UploadMediaService;

/**
 * Integrate wymeditor as a mixin to be used with textarea.
 */
@IncludeJavaScriptLibrary( { "context:/static/js/jquery.timer.js", "context:/static/js/wymeditor/jquery.wymeditor.js",
		"context:/static/js/wymeditor/plugins/fullscreen/jquery.wymeditor.fullscreen.js",
		"context:/static/js/wymeditor/plugins/upload-image-dialog/jquery.wymeditor.upload-image-dialog.js",
		"context:/static/js/wymeditor/plugins/autosave/jquery.wymeditor.autosave.js", "context:/static/js/ajaxupload.js" })
public class WymEditor {

	@Inject
	private UploadMediaService uploadMedia;

	@Inject
	private MultipartDecoder decoder;

	@Inject
	private Messages messages;

	@Inject
	private FormSupport support;

	@Inject
	@Symbol(WookiSymbolsConstants.WOOKI_AUTOSAVE_INTERVAL)
	private int autosaveInterval;

	@Inject
	@Path("context:/static/js/wymeditor/")
	private Asset basePath;

	@Inject
	@Path("context:/static/js/wymeditor/jquery.wymeditor.js")
	private Asset wymPath;

	@Inject
	@Path("context:/static/js/jquery-1.3.2.min.js")
	private Asset jQueryPath;

	@Inject
	@Path("context:/static/img/ajax-loader-min.gif")
	private Asset ajaxLoader;

	@Inject
	private ComponentResources resources;

	@Parameter(defaultPrefix = BindingConstants.ASSET)
	private String wymStyle;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "wooki")
	private String wymSkin;

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

		params.put("skin", wymSkin);

		// Set parameter for production mode compatibility
		params.put("basePath", basePath.toClientURL() + "/");
		params.put("wymPath", wymPath.toClientURL());
		params.put("jQueryPath", jQueryPath.toClientURL());
		params.put("classesHtml", "");
		params.put("ajaxLoader", ajaxLoader.toClientURL());
		params.put("formId", support.getClientId());
		params.put("autosaveInterval", autosaveInterval);

		Link uploadActionLink = resources.createEventLink("uploadImage");
		params.put("uploadAction", uploadActionLink.toAbsoluteURI());

		// Add activation context
		String activationContext = uploadActionLink.getParameterValue(InternalConstants.PAGE_CONTEXT_NAME);
		if (activationContext != null) {
			JSONObject uploadDatas = new JSONObject();
			uploadDatas.put(InternalConstants.PAGE_CONTEXT_NAME, activationContext);
			params.put("uploadDatas", uploadDatas);
		}

		data.put("params", params);

		// Use wymeditor
		renderSupport.addInit("initWymEdit", data);

	}

	/**
	 * Upload image.
	 * 
	 * @return
	 */
	@OnEvent(value = "uploadImage")
	public Object uploadFile() {
		JSONObject result = new JSONObject();
		JSONArray message = new JSONArray();
		try {
			UploadedFile attachment = decoder.getFileUpload("attachment");
			String path = this.uploadMedia.uploadMedia(attachment);
			result.put("error", false);
			result.put("path", path);
			message.put(messages.get("upload-success"));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			result.put("error", true);
			message.put(messages.get("upload-failure"));
		}
		result.put("messages", message);
		return new TextStreamResponse("text/html", result.toString());
	}

}
