package com.wooki.services;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.model.Comment;

/**
 * Use to manage comments in the wooki application.
 * 
 * @author ccordenier
 * 
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	void refuseComment(Comment comment);

	/**
	 * Can change the state of a comment to solved.
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	void solveComment(Comment comment);
}
