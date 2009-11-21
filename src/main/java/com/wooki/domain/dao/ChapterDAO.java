package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Chapter;

/**
 * Handle Chapter persistence.
 * 
 * @author ccordenier
 *
 */
public interface ChapterDAO extends GenericDAO<Chapter, Long> {

	/**
	 * Get the content of a book.
	 *
	 * @param id
	 * @return
	 */
	String getContent(Chapter chapter);

	/**
	 * List existing chapter for a given book.
	 * 
	 * @param bookId
	 * @return
	 */
	List<Chapter> listChapters(Long bookId);

	/**
	 * List the last modified elements.
	 *
	 * @param id The id of the book
	 * @param nbElts The number of elements to return
	 * @return
	 */
	List<Chapter> listLastModified(Long id, int nbElts);

}
