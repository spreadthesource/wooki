package com.wooki.pages.book;

import java.text.SimpleDateFormat;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.Author;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Publication;
import com.wooki.pages.chapter.Edit;
import com.wooki.services.BookManager;
import com.wooki.services.ChapterManager;
import com.wooki.services.utils.DateUtils;

public class Index {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private RenderSupport support;

	@InjectPage
	private Edit editChapter;

	@Property
	private Book book;

	@Property
	private com.wooki.domain.model.Chapter bookAbstract;

	@Property
	private Author currentAuthor;

	@Property
	private int loopIdx;

	@Property
	private SimpleDateFormat format = DateUtils.getDateFormat();

	@Property
	private String abstractContent;

	private Long bookId;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public void setupBook(Long bookId) {
		this.bookId = bookId;
		book = bookManager.findById(bookId);
		Publication published = chapterManager
				.getLastPublishedContent(bookManager.getBookAbstract(book)
						.getId());
		if (published != null) {
			abstractContent = published.getContent();
		}
		bookAbstract = bookManager.getBookAbstract(book);
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long retrieveBookId() {
		return bookId;
	}

	/**
	 * Get edit context for chapter 
	 *
	 * @return
	 */
	public Object[] getEditCtx() {
		return new Object[] { this.bookId, this.bookAbstract.getId() };
	}

	@AfterRender
	void addScript() {
		support.addScript("Wooki.bubbles.init();");
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	/**
	 * Used to verify if it's the last elt in the list.
	 * 
	 * @return
	 */
	public boolean isNotLastAuthor() {
		if (loopIdx == this.book.getAuthors().size()) {
			return true;
		}
		return false;
	}

}
