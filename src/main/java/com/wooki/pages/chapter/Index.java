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
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Display a chapter and provide link to version/revision if needed.
 * 
 * @author ccordenier
 * 
 */
public class Index {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private WookiSecurityContext securityContext;

	@Property
	private Long bookId;

	private Long chapterId;

	@Property
	private Book book;

	@Property
	private Chapter chapter;

	@Property
	private String content;

	@Property
	private Long publicationId;

	@Property
	private String revision;

	@Property
	private boolean availableContent;

	@Property
	private boolean viewingRevision;

	@Property
	private Long previous;

	@Property
	private String previousTitle;

	@Property
	private Long next;

	@Property
	private String nextTitle;

	@InjectPage
	private com.wooki.pages.book.Index bookIndex;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {

		// Check parameter numbers
		if (ctx.getCount() < 2) {
			return com.wooki.pages.Index.class;
		}

		this.bookId = ctx.get(Long.class, 0);
		this.chapterId = ctx.get(Long.class, 1);

		if (ctx.getCount() > 2) {
			this.revision = ctx.get(String.class, 2);

			if (!securityContext.isLoggedIn() || !securityContext.isAuthorOfBook(bookId) || !this.revision.equals("workingcopy"))
				return redirectToBookIndex();

			this.viewingRevision = true;
		}

		return null;
	}

	@SetupRender
	public void setupDisplay() {

		// Get book related information
		this.book = this.bookManager.findById(bookId);
		this.chapter = this.chapterManager.findById(chapterId);

		Object[] data = this.chapterManager.findPrevious(this.bookId, this.chapterId);
		if (data != null && data.length == 2) {
			this.previous = (Long) data[0];
			this.previousTitle = (String) data[1];
		}

		data = this.chapterManager.findNext(this.bookId, this.chapterId);
		if (data != null && data.length == 2) {
			this.next = (Long) data[0];
			this.nextTitle = (String) data[1];
		}

		// Get the publication
		Publication publication = (this.revision != null) ? this.chapterManager.getLastPublication(chapterId) : this.chapterManager
				.getLastPublishedPublication(this.chapterId);
		if (publication != null) {
			this.content = publication.getContent();
			this.publicationId = publication.getId();
		}

		if (this.content != null) {
			availableContent = true;
		}
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		return new Object[] { this.bookId, this.chapterId };
	}

	public Object[] getEditCtx() {
		return new Object[] { this.bookId, this.chapterId };
	}

	public Object[] getPreviousCtx() {
		if (this.viewingRevision) {
			return new Object[] { this.bookId, this.previous, "workingcopy" };
		}
		return new Object[] { this.bookId, this.previous };
	}

	public Object[] getNextCtx() {
		if (this.viewingRevision) {
			return new Object[] { this.bookId, this.next, "workingcopy" };
		}
		return new Object[] { this.bookId, this.next };
	}

	private final Object redirectToBookIndex() {
		bookIndex.setBookId(bookId);
		return bookIndex;
	}

	public boolean isShowNext() {
		return this.next != null;
	}

	public boolean isShowPrevious() {
		return this.previous != null;
	}
}
