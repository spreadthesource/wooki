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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Display all the comment for a given chapter.
 * 
 * @author ccordenier
 * 
 */
public class Issues extends BookBase {

	public static final String ALL = "all";

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private WookiSecurityContext securityCtx;

	@Property
	private List<Chapter> chapters;

	@Property
	private Chapter chapter;

	@Property
	private int loopIdx;

	private Long chapterId;

	private String request;

	@SetupRender
	public void setupNav() {
		setLeft(createPageMenuItem("< Table of content", "book/index", false, getBookId()));
		setCenter(createPageMenuItem(getBook().getTitle(), "book/index", false, getBookId()));
	}

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupChapter(Long bookId, String request) {

		this.request = request;

		if (ALL.equals(request)) {
			this.chapters = this.chapterManager.listChaptersInfo(bookId);
		} else {
			this.chapters = new ArrayList<Chapter>();
			Long chapterId = Long.parseLong(request);

			// Get book related information
			this.chapterId = chapterId;
			chapter = this.chapterManager.findById(chapterId);
			if (chapter == null) {
				return redirectToBookIndex();
			}

			this.chapters.add(chapter);

		}

		return null;
	}

	/**
	 * Return when displaying issues for first chapter.
	 * 
	 * @return
	 */
	public boolean isAbstractChapter() {
		return this.loopIdx == 0;
	}

	public Object[] getChapterCtx() {
		return new Object[] { this.getBookId(), this.chapter.getId() };
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		return new Object[] { this.getBookId(), this.request };
	}

}
