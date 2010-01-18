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

package com.wooki.pages.chapter;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.pages.book.Index;

/**
 * This page is used to update/publish a chapter of a given book.
 * 
 * @author ccordenier
 * 
 */
public class Edit extends BookBase {

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private Block titleBlock;
	
	@InjectPage
	private Index index;

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
	}

	@OnEvent(value = EventConstants.SUCCESS, component ="updateTitle")
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

	public void onCancel() {
		this.cancel = true;
	}

	/**
	 * Update content and publish if requested.
	 * 
	 * @return The book index page
	 */
	@OnEvent(value = EventConstants.SUCCESS, component = "editChapterForm")
	public Object updateChapter() {
		if (!cancel) {
			chapterManager.updateContent(chapterId, data);
			if (publish) {
				chapterManager.publishChapter(chapterId);
			}
		}

		index.setBookId(this.getBookId());
		return index;
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

}
