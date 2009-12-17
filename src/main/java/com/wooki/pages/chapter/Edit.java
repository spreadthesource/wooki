package com.wooki.pages.chapter;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.validator.Required;

import com.wooki.domain.biz.ChapterManager;
import com.wooki.pages.book.Index;

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
	@Validate("required")
	private String data;

	private boolean publish;

	@OnEvent(value = EventConstants.ACTIVATE)
	public void onActivate(Long bookId, Long chapterId) {
		this.bookId = bookId;
		this.chapterId = chapterId;
	}

	@OnEvent(value = EventConstants.PREPARE_FOR_RENDER)
	public void prepareFormData() {
		this.data = chapterManager.getLastContent(chapterId);
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

}
