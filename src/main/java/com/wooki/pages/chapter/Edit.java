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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.pages.book.Index;

/**
 * This page is used to update/publish a chapter of a given book.
 * 
 * @author ccordenier
 * 
 */
public class Edit {

	@Inject
	private ChapterManager chapterManager;

	@InjectPage
	private Index index;

	private Long bookId;

	private Long chapterId;

	@Property
	private Chapter chapter;

	@Property
	@Validate("required")
	private String data;

	private boolean publish;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object onActivate(EventContext ctx) {

		if (ctx.getCount() != 2) {
			return com.wooki.pages.Index.class;
		}

		try {
			this.bookId = ctx.get(Long.class, 0);
			this.chapterId = ctx.get(Long.class, 1);
		} catch (RuntimeException re) {
			return com.wooki.pages.Index.class;
		}
		
		this.bookId = bookId;
		this.chapterId = chapterId;

		this.chapter = chapterManager.findById(chapterId);
		return null;
	}

	@SetupRender
	public void prepareFormData() {
		this.data = chapterManager.getLastContent(chapterId);
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveIds() {
		return new Object[] { this.bookId, this.chapterId };
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
		chapterManager.updateContent(chapterId, data);
		if (publish) {
			chapterManager.publishChapter(chapterId);
		}
		index.setBookId(bookId);
		return index;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
	}

}
