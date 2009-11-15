package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Activity;

/**
 * Manipulate activity elements
 * 
 * @author ccordenier
 * 
 */
public interface ActivityDAO {

	/**
	 * Create a new element in activity table.
	 * 
	 * @param activity
	 * @return
	 */
	Activity add(Activity activity);

	/**
	 * List the last nbElelements activities.
	 * 
	 * @param nbElements
	 * @return
	 */
	List<Activity> list(int nbElements);

	/**
	 * List the last nbElelements activities.
	 * 
	 * @param nbElements
	 * @return
	 */
	List<Activity> listForBook(Long bookId, int nbElements);

	/**
	 * List the last nbElelements activities.
	 * 
	 * @param nbElements
	 * @return
	 */
	List<Activity> listByChapter(Long chapterId);
}
