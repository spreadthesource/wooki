//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
	 * List comment on activities.
	 *
	 * @param commentId
	 * @return
	 */
	List<Activity> listAllActivitiesOnComment(Long commentId);
	
	/**
	 * Get all the activities on a given chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	List<Activity> listAllActivitiesOnChapter(Long chapterId);
	
	/**
	 * Get all the activities on a given chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	List<Activity> listAllActivitiesOnBook(Long bookId);
	
	/**
	 * List the last nbElelements activities.
	 * @param startIdx TODO
	 * @param nbElements
	 * 
	 * @return
	 */
	List<Activity> list(int startIdx, int nbElements);

	/**
	 * List the book creation activity.
	 * @param startIdx TODO
	 * 
	 * @return
	 */
	List<Activity> listBookCreationActivity(int startIdx, int nbElements);

	/**
	 * List the activity of a user on its own books.
	 * @param startIdx TODO
	 * @param nbElementsn
	 * @param userId
	 * 
	 * @return
	 */
	List<Activity> listActivityOnBook(int startIdx, int nbElementsn, Long userId);

	/**
	 * List the user public activity.
	 * @param startIdx TODO
	 * @param nbElts
	 * @param userId
	 * 
	 * @return
	 */
	List<Activity> listUserActivity(int startIdx, int nbElts, Long userId);

	/**
	 * List the activity of others users on the current user book.
	 * @param startIdx TODO
	 * @param nbElts
	 * @param userId
	 * 
	 * @return
	 */
	List<Activity> listActivityOnUserBooks(int startIdx, int nbElts, Long userId);

	/**
	 * List account activity on wooki.
	 * @param startIdx TODO
	 * @param nbElts
	 * @param userId
	 * 
	 * @return
	 */
	List<Activity> listAccountActivity(int startIdx, int nbElts);

}
