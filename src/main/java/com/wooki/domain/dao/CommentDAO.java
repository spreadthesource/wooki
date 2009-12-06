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
	List<Comment> listOpenForPublication(Long chapterId);

	/**
	 * List the comments number by block for a given publication.
	 * 
	 * @return
	 */
	List<Object[]> listCommentsInforForPublication(Long publicationId);

	/**
	 * List all the comments associated to a domId in a given publication.
	 *
	 * @param publicationId
	 * @param domId
	 * @return
	 */
	List<Comment> listOpenForPublicationAndDomId(Long publicationId,
			String domId);
}
