package com.wooki.services;

import java.util.List;

import com.wooki.domain.model.Comment;

/**
 * Use to manage comments in the wooki application.
 * 
 * @author ccordenier
 * 
 */
public interface CommentManager {
	
	/**
	 * List all the comment for a book including comments associated to
	 * chapters.
	 * 
	 * @param bookId
	 * @return
	 */
	List<Comment> listAll(Long bookId);

	/**
	 * List all the comments associated the given chapter.
	 * 
	 * @return
	 */
	List<Comment> listOpenForPublication(Long chapterId);

	/**
	 * 
	 * @param chapterId
	 * @return
	 */
	List<Object[]> listCommentInfos(Long chapterId);

	/**
	 * Can change the state of a comment to solved.
	 */
	void refuseComment(Comment comment);

	/**
	 * Can change the state of a comment to solved.
	 */
	void solveComment(Comment comment);
}
