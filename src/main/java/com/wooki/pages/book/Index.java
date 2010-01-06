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

package com.wooki.pages.book;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Delegate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.pages.chapter.Edit;
import com.wooki.services.BookStreamResponse;
import com.wooki.services.ExportService;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This page displays a book with its table of contents.
 */
public class Index extends BookBase {
 
	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private WookiSecurityContext securityCtx;
	
	@Inject
	private ExportService exportService;

	@Inject
	private Block addChapterLink;

	@Inject
	private Block addChapterForm;

	@Component
	private Delegate addChapterDelegate;

	@InjectPage
	private Edit editChapter;

	@Property
	private Long bookAbstractId;

	@Property
	private User currentUser;

	@Property
	private int loopIdx;

	@Property
	private List<User> authors;

	@Property
	private List<Chapter> chaptersInfo;

	@Property
	private Chapter currentChapter;

	/**
	 * Will be set if the author tries to add a new chapter
	 */
	@Property
	private String chapterName;

	@Property
	private Block addChapterToggle;
	
	private boolean showWorkingCopyLink;

	private boolean bookAuthor;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {

		super.setupBook(ctx);
		
		if (ctx.getCount() > 1) {
			String revision = ctx.get(String.class, 1);

			if (!this.securityCtx.isLoggedIn()
					|| !this.securityCtx.isAuthorOfBook(this.getBookId())
					|| !"workingcopy".equals(revision))
				return com.wooki.pages.Index.class;

			this.setViewingRevision(true);
		}

		return null;
	}

	/**
	 * Prepare book display.
	 *
	 */
	@SetupRender
	public void setupBookDisplay() {
		
		this.authors = this.getBook().getAuthors();

		// List chapter infos
		List<Chapter> chapters = chapterManager.listChaptersInfo(this.getBookId());
		this.bookAbstractId = chapters.get(0).getId();

		if (chapters.size() > 0) {
			this.chaptersInfo = chapters.subList(1, chapters.size());
		}

		// Setup abstract content
		this.setupContent(this.bookAbstractId, this.isViewingRevision());
		
		this.bookAuthor = this.securityCtx.isAuthorOfBook(this.getBookId());
		if (this.bookAuthor) {
			this.addChapterToggle = this.addChapterLink;
		}
		
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "addChapterForm")
	public Object addNewChapter() {
		Chapter chapter = bookManager.addChapter(this.getBook(), chapterName);
		editChapter.setBookId(this.getBookId());
		editChapter.setChapterId(chapter.getId());
		return editChapter;
	}


	@OnEvent(value = EventConstants.ACTION, component = "showAddChapterField")
	public Object toggleAddChapter() {
		this.addChapterToggle = addChapterForm;
		return addChapterDelegate;
	}

	/**
	 * Simply export to PDF.
	 * 
	 * @return
	 */
	@OnEvent(value = "print")
	public StreamResponse exportPdf() {
		return new BookStreamResponse(this.exportService, this.getBookId(), this.getBook().getSlugTitle());
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long retrieveBookId() {
		return this.getBookId();
	}
	
	public boolean isPublished() {
		long chapterId = currentChapter.getId();

		Publication publication = this.chapterManager
				.getLastPublishedPublication(chapterId);

		return (publication != null);
	}

	public boolean isAbstractHasWorkingCopy() {
		return hasWorkingCopy(this.bookAbstractId);
	}

	public boolean isShowWorkingCopyLink() {
		long chapterId = currentChapter.getId();
		return hasWorkingCopy(chapterId);
	}

	private final boolean hasWorkingCopy(long chapterId) {
		Publication publication = this.chapterManager
				.getLastPublication(chapterId);
		if (publication != null) {
			boolean workingCopy = !publication.isPublished();
			return bookAuthor && workingCopy;
		}
		return false;
	}

	/**
	 * Get edit context for chapter
	 * 
	 * @return
	 */
	public Object[] getEditCtx() {
		return new Object[] { this.getBookId(), this.bookAbstractId };
	}

	public Object[] getAbstractWorkingCopyCtx() {
		return new Object[] { this.getBookId(), "workingcopy" };
	}

	/**
	 * Get id to link to chapter display
	 * 
	 * @return
	 */
	public Object[] getChapterCtx() {
		return new Object[] { this.getBookId(), this.currentChapter.getId() };
	}

	public Object[] getChapterWorkingCopyCtx() {
		return new Object[] { this.getBookId(), this.currentChapter.getId(),
				"workingcopy" };
	}

}
