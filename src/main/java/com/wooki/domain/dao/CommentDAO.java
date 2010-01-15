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

import com.wooki.domain.model.Comment;

/**
 * DAO in charge of the comment class.
 * 
 * @author ccordenier
 * 
 */
public interface CommentDAO extends GenericDAO<Comment, Long> {

	/**
	 * Check if an author is owner of a book.
	 *
	 * @param commId
	 * @param username
	 * @return
	 */
	boolean isOwner(Long commId, String username);
	
	/**
	 * Get the list of open issues for a chapter.
	 * 
	 * @param chapterId
	 * @return
	 */
	List<Comment> listForPublication(Long chapterId);

	/**
	 * List the comments number by block for a given publication.
	 * 
	 * @return
	 */
	List<Object[]> listCommentsInfoForPublication(Long publicationId);

	/**
	 * List all the comments associated to a domId in a given publication.
	 *
	 * @param publicationId
	 * @param domId
	 * @return
	 */
	List<Comment> listForPublicationAndDomId(Long publicationId,
			String domId);
	
	/**
	 * List all the comment for a given chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	List<Comment> listForChapter(Long chapterId);
}
