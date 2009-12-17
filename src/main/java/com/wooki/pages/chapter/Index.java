package com.wooki.pages.chapter;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
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

	private Long bookId;

	private Long chapterId;

	@Property
	private Book book;

	@Property
	private Chapter chapter;

	@Property
	private String content;

	@Property
	private String revision;

	@Property
	private boolean availableContent;

	@Property
	private boolean viewingRevision;

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

			if (!securityContext.isLoggedIn() || !securityContext.isAuthorOfBook(bookId) || this.revision != "workingcopy")
				redirectToBookIndex();

			this.viewingRevision = true;
		}

		// Get book related information
		this.book = this.bookManager.findById(bookId);
		this.chapter = this.chapterManager.findById(chapterId);
		this.content = (this.revision != null) ? this.chapterManager.getLastContent(chapterId) : this.chapterManager.getLastPublishedContent(this.chapterId);

		if (this.content != null) {
			availableContent = true;
		}

		return null;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		return new Object[] { this.bookId, this.chapterId };
	}

	private final Object redirectToBookIndex() {
		bookIndex.setBookId(bookId);
		return bookIndex;
	}
}
