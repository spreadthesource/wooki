package com.wooki.services;

import java.util.List;

import com.wooki.domain.model.User;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.Publication;

/**
 * Interface used to access to chapter related information from the wooki
 * application.
 * 
 * @author ccordenier
 * 
 */
public interface ChapterManager {

	/**
	 * Add a comment to the list of existing one.
	 * 
	 * @param chapter
	 * @param content
	 * @param domId
	 *            TODO
	 */
	Comment addComment(Chapter chapter, User author, String content,
			String domId);

	/**
	 * Load a chapter.
	 *
	 * @return
	 */
	Chapter findById(Long chapterId);
	
	/**
	 * Retrieve the content for a given chapter. Content is lazy loaded for the
	 * sake of performance.
	 * 
	 * @param chapter
	 * @return
	 */
	String getContent(Long chapterId);

	/**
	 * Publish chapter content
	 * 
	 * @param chapterId
	 */
	void publishChapter(Long chapter);

	/**
	 * Find the last published chapter.
	 * 
	 * @param chapterId
	 * @return
	 */
	String getLastPublishedContent(Long chapterId);

	/**
	 * When a chapter content is update then all its related comments must be
	 * re-organized for the sake of consistency.
	 * 
	 * @param chapter
	 */
	void updateContent(Long chapterId, String content);

	/**
	 * Remove chapter from book.
	 * 
	 * @param book
	 * @param chapter
	 */
	void delete(Chapter chapter);

	/**
	 * List the chapters for a given book.
	 * 
	 * @param bookId
	 * @return
	 */
	List<Chapter> listChapters(Long bookId);

	/**
	 * List the chapters for a given book, the book abstract chapter is exclude
	 * from this list.
	 * 
	 * @param bookId
	 * @return
	 */
	List<Chapter> listChaptersInfo(Long bookId);

}
