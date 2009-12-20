package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.activity.Activity;

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
	 * List the book creation activity.
	 *
	 * @return
	 */
	List<Activity> listBookCreationActivity(int nbElements);

	/**
	 * List the activity of a user on its own books.
	 *
	 * @param nbElementsn
	 * @param userId
	 * @return
	 */
	List<Activity> listActivityOnBook(int nbElementsn, Long userId);
	
	/**
	 * List the user public activity.
	 *
	 * @param nbElts
	 * @param userId
	 * @return
	 */
	List<Activity> listUserActivity(int nbElts, Long userId);

	/**
	 * List the activity of others users on the current user book. 
	 *
	 * @param nbElts
	 * @param userId
	 * @return
	 */
	List<Activity> listActivityOnUserBooks(int nbElts, Long userId);

}
