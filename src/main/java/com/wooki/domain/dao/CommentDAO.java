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
	 * List all the comments for a given chapter.
	 *
	 * @return
	 */
	List<Comment> listForChapter(Long chapterId);

	/**
	 * Get the list of open issues for a chapter.
	 * 
	 * @param chapterId
	 * @return
	 */
	public List<Comment> listOpenForChapter(Long chapterId);

	/**
	 * List the comments by book
	 * 
	 * @return
	 */
	List<Comment> listForBook(Long book);

}
