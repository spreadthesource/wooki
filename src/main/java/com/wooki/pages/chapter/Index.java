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

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.base.components.BookMenuItem;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.services.HttpError;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Display a chapter and provide link to version/revision if needed.
 * 
 * @author ccordenier
 */
public class Index extends BookBase {

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private WookiSecurityContext securityCtx;

	@Inject
	private RequestPageCache pageCache;

	private Long chapterId;

	@Property
	private Chapter chapter;

	@Property
	private Long previous;

	@Property
	private String previousTitle;

	@Property
	private Long next;

	@Property
	private String nextTitle;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupChapter(Long bookId, Long chapterId, String revision) {

		this.setViewingRevision(true);
		this.setRevision(revision);

		// Setup chapter
		this.setupChapter(bookId, chapterId);

		if (ChapterManager.LAST.equalsIgnoreCase(revision) && !(this.securityCtx.isLoggedIn() && this.securityCtx.isAuthorOfBook(this.getBookId()))) {
			return new HttpError(403, "Access denied");
		}

		this.setPublication(this.chapterManager.getRevision(chapterId, revision));
		if (this.getPublication() == null) {
			return new HttpError(404, "Revision not found");
		}

		return true;
	}

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupChapter(Long bookId, Long chapterId) {
		this.setBookId(bookId);

		// Get book related information
		this.chapterId = chapterId;
		this.chapter = this.chapterManager.findById(chapterId);
		if (this.chapter == null) {
			return new HttpError(404, "Chapter not found");
		}

		this.setPublication(this.chapterManager.getLastPublishedPublication(chapterId));
		if (this.getPublication() == null) {
			return new HttpError(404, "Chapter not found");
		}

		// send 404 if trying to see abstract
		List<Chapter> chapters = chapterManager.listChaptersInfo(this.getBookId());
		if (!this.isViewingRevision() && chapterId.equals(chapters.get(0).getId())) {
			return new HttpError(404, "Chapter not found");
		}

		return true;
	}

	@SetupRender
	public Object setupDisplay() {

		this.setupContent();

		if (!this.isViewingRevision()) {
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

		return null;

	}

	@SetupRender
	public void setupMenus() {
		if (securityCtx.isAuthorOfBook(getBookId())) {
			if (isShowAdmin()) {
				getAdminActions().add(createPageMenuItem("Edit content", "chapter/edit", false, getBookId(), chapterId));

				BookMenuItem delete = createEventMenuItem("Delete", pageCache.get("chapter/index"), null, "delete", false	);
				delete.setConfirm(true);
				getAdminActions().add(delete);
			}
		}
		getMenu().add(createPageMenuItem("All feedback", "chapter/issues", false, this.getBookId(), Issues.ALL));
		getMenu().add(createPageMenuItem("Feedback on this chapter only", "chapter/issues", false, getBookId(), chapterId));
	}

	@SetupRender
	public void setupNav() {
		if ((previous != null) && (previousTitle != null)) {
			setLeft(createPageMenuItem("< " + previousTitle, "chapter/index", false, getBookId(), previous));
		} else {
			setLeft(createPageMenuItem("< Table of content", "book/index", false, getBookId()));
		}

		if ((next != null) && (nextTitle != null))
			setRight(createPageMenuItem(nextTitle + " >", "chapter/index", false, getBookId(), next));

		setCenter(createPageMenuItem(getBook().getTitle(), "book/index", false, getBookId()));
	}

	@OnEvent(value = "delete")
	public Object deleteChapter(Long boodId, Long chapterId) {
		this.chapterManager.remove(chapterId);
		return this.redirectToBookIndex();
	}

	public String getTitle() {
		return this.getBook().getTitle() + " - " + this.chapter.getTitle();
	}

	public Object[] getAllIssuesCtx() {
		return new Object[] { this.getBookId(), Issues.ALL };
	}

	public Object[] getChapIssuesCtx() {
		return new Object[] { this.getBookId(), this.chapterId };
	}

	/**
	 * Get context for previous link.
	 * 
	 * @return Book id, previous chapter id and revision
	 */
	public Object[] getPreviousCtx() {
		if (this.isViewingRevision()) {
			return new Object[] { this.getBookId(), this.previous, ChapterManager.LAST };
		}
		return new Object[] { this.getBookId(), this.previous };
	}

	/**
	 * Get context for next link.
	 * 
	 * @return Book id, previous chapter id and revision
	 */
	public Object[] getNextCtx() {
		if (this.isViewingRevision()) {
			return new Object[] { this.getBookId(), this.next, ChapterManager.LAST };
		}
		return new Object[] { this.getBookId(), this.next };
	}

	public boolean isShowAdmin() {
		return !this.isViewingRevision() || ChapterManager.LAST.equals(this.getRevision());
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		if (this.getRevision() != null) {
			return new Object[] { this.getBookId(), this.chapterId, this.getRevision() };
		}
		return new Object[] { this.getBookId(), this.chapterId };
	}

}
