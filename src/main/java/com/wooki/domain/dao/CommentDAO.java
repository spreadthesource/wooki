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
	 * Get the list of open issues for a chapter.
	 * 
	 * @param chapterId
	 * @return
	 */
	public List<Comment> listOpenForPublication(Long chapterId);

	/**
	 * List the comments number by block for a given publication.
	 *
	 * @return
	 */
	public List<Object[]> listCommentsInforForPublication(Long publicationId);
	
}
