package com.wooki.pages.book;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.pages.chapter.Edit;
import com.wooki.services.BookManager;
import com.wooki.services.ChapterManager;
import com.wooki.services.utils.DateUtils;

/**
 * This page displays a book with its table of contents. 
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
	private RenderSupport support;

	@InjectPage
	private Edit editChapter;

	@Property
	private Book book;

	@Property
	private Long bookAbstractId;

	@Property
	private User currentUser;

	@Property
	private int loopIdx;

	@Property
	private SimpleDateFormat format = DateUtils.getDateFormat();

	@Property
	private String abstractContent;

	@Property
	private List<User> authors;

	@Property
	private List<Chapter> chaptersInfo;

	@Property
	private Chapter currentChapter;

	private Long bookId;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public void setupBook(Long bookId) {
		this.bookId = bookId;

		// Get book related information
		this.book = bookManager.findById(bookId);
		this.authors = book.getAuthors();

		// List chapter infos
		List<Chapter> chapters = chapterManager.listChaptersInfo(bookId);
		this.bookAbstractId = chapters.get(0).getId();

		if (chapters.size() > 0) {
			this.chaptersInfo = chapters.subList(1, chapters.size());
		}

		// Get abstract content to display
		Publication published = chapterManager
				.getLastPublishedContent(this.bookAbstractId);
		if (published != null) {
			try {
				this.abstractContent = new String(published.getContent(),
						"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
		return new Object[] { this.bookId, this.bookAbstractId };
	}

	/**
	 * Get id to link to chapter display
	 * 
	 * @return
	 */
	public Object[] getChapterCtx() {
		return new Object[] { this.bookId, this.currentChapter.getId() };
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
		if (loopIdx < this.authors.size() - 1) {
			return true;
		}
		return false;
	}

}
