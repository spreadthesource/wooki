package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Activity;

/**
 * Manipulate activity elements
 * 
 * @author ccordenier
 * 
 */
public interface ActivityDAO extends GenericDAO<Activity, Long> {

	/**
	 * List the last nbElelements activities.
	 * 
	 * @param nbElements
	 * @return
	 */
	List<Activity> list(int nbElements);

	/**
	 * List the last nbElelements activities for a given book.
	 *
	 * @param nbElements
	 * @return
	 */
	List<Activity> listForBook(Long bookId, int nbElements);

	/**
	 * List the last nbElelements activities for a corresponding chapter
	 * 
	 * @param nbElements
	 * @return
	 */
	List<Activity> listByChapter(Long chapterId);
}
