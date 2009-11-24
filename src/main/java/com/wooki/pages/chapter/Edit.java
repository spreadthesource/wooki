package com.wooki.pages.chapter;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.pages.book.Index;
import com.wooki.services.ChapterManager;

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
	private String data;

	private boolean publish;

	@Persist
	private Link from;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(Long bookId, Long chapterId) {
		this.bookId = bookId;
		this.chapterId = chapterId;
		this.data = chapterManager.getContent(chapterId);
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
		if (from != null) {
			return from;
		}
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

	public Link getFrom() {
		return from;
	}

	public void setFrom(Link from) {
		this.from = from;
	}

}
