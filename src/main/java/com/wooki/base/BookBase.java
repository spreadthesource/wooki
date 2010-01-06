package com.wooki.base;

import java.text.SimpleDateFormat;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Publication;
import com.wooki.services.utils.DateUtils;

/**
 * Abstract index page to share common code.
 * 
 * @author ccordenier
 * 
 */
public class BookBase {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@InjectPage
	private com.wooki.pages.book.Index bookIndex;

	private SimpleDateFormat format = DateUtils.getDateFormat();

	private Book book;

	private Long bookId;

	private String content;

	private Long publicationId;

	private boolean viewingRevision;

	private String revision;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {

		// Check parameter numbers
		if (ctx.getCount() < 1) {
			return com.wooki.pages.Index.class;
		}

		this.bookId = ctx.get(Long.class, 0);

		// Check resource exists
		this.book = this.bookManager.findById(this.bookId);

		if (this.book == null) {
			return com.wooki.pages.Index.class;
		}

		return null;
	}

	/**
	 * Return true if this is the last row. 
	 *
	 * @param idx
	 * @param maxIdx
	 * @return
	 */
	public boolean isLastLoop(int idx, int maxIdx) {
		return idx == maxIdx - 1;
	}

	/**
	 * This method must be called to initialize the display of a chapter. It can
	 * be the abstract for the index page or the content of a chapter.
	 * 
	 */
	protected void setupContent(Long chapterId, boolean showRevision) {

		// Get the publication
		Publication publication = (showRevision) ? this.chapterManager.getLastPublication(chapterId) : this.chapterManager
				.getLastPublishedPublication(chapterId);
		if (publication != null) {
			this.content = publication.getContent();
			this.publicationId = publication.getId();
		}

	}

	protected final Object redirectToBookIndex() {
		bookIndex.setBookId(this.bookId);
		return bookIndex;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}

	public boolean isViewingRevision() {
		return viewingRevision;
	}

	public void setViewingRevision(boolean viewRevision) {
		this.viewingRevision = viewRevision;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public SimpleDateFormat getFormat() {
		return format;
	}

	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

}
