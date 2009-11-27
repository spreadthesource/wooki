package com.wooki.pages.chapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.services.BookManager;
import com.wooki.services.ChapterManager;

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

	private Long bookId;

	private Long chapterId;

	@Property
	private Book book;

	@Property
	private Chapter chapter;

	@Property
	private String content;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void setupBook(Long bookId, Long chapterId) {

		this.bookId = bookId;
		this.chapterId = chapterId;

		// Get book related information
		this.book = this.bookManager.findById(bookId);
		this.chapter = this.chapterManager.findById(chapterId);
		Publication published = this.chapterManager
				.getLastPublishedContent(chapterId);
		if (published != null && published.getContent() != null) {
			try {
				this.content = new String(published.getContent(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		return new Object[] { this.bookId, this.chapterId };
	}
}
