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

package com.wooki.pages.chapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.apache.tapestry5.util.TextStreamResponse;

import com.ibm.icu.util.Calendar;
import com.wooki.BookMenuItem;
import com.wooki.NavLinkPosition;
import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.exception.PublicationXmlException;
import com.wooki.domain.model.Chapter;
import com.wooki.services.LinkSupport;

/**
 * This page is used to update/publish a chapter of a given book.
 * 
 * @author ccordenier
 */
@IncludeJavaScriptLibrary( { "context:/static/js/jquery.notifyBar.js", "context:/static/js/notifybar.js" })
@IncludeStylesheet("context:/static/css/jquery.notifyBar.css")
public class Edit extends BookBase {

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private Block titleBlock;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	@Inject
	private LinkSupport linkSupport;

	@InjectComponent
	private Form editChapterForm;

	@InjectPage
	private Index index;

	@Inject
	@Symbol(UploadSymbols.FILESIZE_MAX)
	private long maxFileSize;

	private Long chapterId;

	@Property
	private Chapter chapter;

	@Property
	@Validate("required")
	private String data;

	@Property
	private Long previous;

	@Property
	private String previousTitle;

	@Property
	private Long next;

	@Property
	private String nextTitle;

	@Property
	private boolean abstractChapter;

	private DateFormat format = new SimpleDateFormat("hh:mm");

	private boolean publish;

	private boolean cancel;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object onActivate(Long bookId, Long chapterId) {

		this.chapterId = chapterId;
		this.chapter = chapterManager.findById(chapterId);

		if (this.chapter == null) {
			return redirectToBookIndex();
		}

		return null;
	}

	@SetupRender
	public void prepareFormData() {
		this.data = chapterManager.getLastContent(chapterId);
		// Check if we are editing the abstract chapter
		if (this.getBook().getChapters() != null && this.getBook().getChapters().size() > 0
				&& this.getBook().getChapters().get(0).getId().equals(this.chapterId)) {
			this.abstractChapter = true;
		}
		
		// Prepare previous and next links
		Object[] data = this.chapterManager.findPrevious(this.getBookId(), this.chapterId);
		if (data != null && data.length == 2) {
			this.previous = (Long) data[0];
			this.previousTitle = (String) data[1];
		}

		data = this.chapterManager.findNext(this.getBookId(), this.chapterId);
		if (data != null && data.length == 2) {
			this.next = (Long) data[0];
			this.nextTitle = (String) data[1];
		}
	}

	@SetupRender
	public void setupNav() {
		if ((previous != null) && (previousTitle != null)) {
			BookMenuItem item = this.linkSupport.createNavLink(NavLinkPosition.LEFT, "< " + previousTitle, "chapter/index", getBookId(), previous);
			item.setConfirm(true);
			item.setConfirmMsg("Cancel edition?");
		} else {
			BookMenuItem item = this.linkSupport.createNavLink(NavLinkPosition.LEFT, "< Table of content", "book/index", getBookId());
			item.setConfirm(true);
			item.setConfirmMsg("Cancel edition?");
		}

		if ((next != null) && (nextTitle != null)) {
			BookMenuItem item = this.linkSupport.createNavLink(NavLinkPosition.RIGHT, nextTitle + " >", "chapter/index", getBookId(), next);
			item.setConfirm(true);
			item.setConfirmMsg("Cancel edition?");
		}

		BookMenuItem item = this.linkSupport.createNavLink(NavLinkPosition.CENTER, getBook().getTitle(), "book/index", getBookId());
		item.setConfirm(true);
		item.setConfirmMsg("Cancel edition?");
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "updateTitle")
	public Object updateTitle() {
		this.chapterManager.update(chapter);
		return this.titleBlock;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveIds() {
		return new Object[] { this.getBookId(), this.chapterId };
	}

	/**
	 * Used to check which submit button has been clicked
	 */
	public void onPublish() {
		this.publish = true;
	}

	/**
	 * Used to check which submit button has been clicked
	 */
	public void onUpdate() {
		this.publish = false;
	}

	/**
	 * Update content and publish if requested.
	 * 
	 * @return The book index page
	 */
	@OnEvent(value = EventConstants.SUCCESS, component = "editChapterForm")
	public Object updateChapter() {

		// If autosave then save and return
		if (request.isXHR()) {
			chapterManager.updateContent(chapterId, data);
			JSONObject result = new JSONObject();
			result.put("message", messages.format("autosave-success", format.format(Calendar.getInstance().getTime())));
			return result;
		}

		try {
			if (!cancel) {
				chapterManager.updateContent(chapterId, data);
				if (publish) {
					chapterManager.publishChapter(chapterId);
				}
			}

			index.setBookId(this.getBookId());

			if (publish) {
				index.setupChapter(this.getBookId(), chapterId);
			} else {
				index.setupChapter(this.getBookId(), chapterId, ChapterManager.LAST);
				index.setRevision(ChapterManager.LAST);
			}

			return index;

		} catch (PublicationXmlException pxEx) {
			editChapterForm.recordError(messages.get("publication-exception"));
			return this;
		}

	}

	public Object[] getCancelCtx() {
		return new Object[] { this.getBookId(), this.chapterId };
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

	/**
	 * Handle upload exception
	 * 
	 * @param ex
	 * @return
	 */
	public Object onUploadException(FileUploadException ex) {
		JSONObject result = new JSONObject();
		JSONArray message = new JSONArray();
		result.put("error", true);
		message.put(messages.format("upload-exception", maxFileSize / 1024));
		result.put("messages", message);
		return new TextStreamResponse("text/html", result.toString());
	}

}
