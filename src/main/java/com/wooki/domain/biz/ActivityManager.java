package com.wooki.domain.biz;

import java.util.List;

import com.wooki.domain.model.activity.Activity;

/**
 * Use to handle activities on wikies element to follow history.
 * 
 * @author ccordenier
 * 
 */
public interface ActivityManager {

	List<Activity> listAll(int nbElts);

	List<Activity> listUserActivity(int nbElts, Long userId);
	
	List<Activity> listActivityOnUserBooks(int nbElts, Long userId);
	
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
	List<Activity> listActivityOnBook(int nbElements, Long userId);
}
