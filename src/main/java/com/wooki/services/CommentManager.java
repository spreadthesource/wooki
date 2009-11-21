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
	List<Comment> listOpenForChapter(Long chapterId);

	/**
	 * List all the open issues for a book. It does not include chapters related
	 * comments.
	 * 
	 * @param bookId
	 * @return
	 */
	List<Comment> listOpenForBook(Long bookId);

	/**
	 * Can change the state of a comment to solved.
	 */
	void refuseComment(Comment comment);

	/**
	 * Can change the state of a comment to solved.
	 */
	void solveComment(Comment comment);
}
