package com.wooki.domain.biz;

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
	 * Update the comment
	 *
	 * @param comment
	 * @return
	 */
	Comment update(Comment comment);
	
	/**
	 * Remove a comment.
	 *
	 * @param commId
	 */
	void removeComment(Long commId);
	
	/**
	 * Find a comment by id.
	 *
	 * @param commId
	 * @return
	 */
	Comment findById(Long commId);
	
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
	 * 
	 * @param chapterId
	 * @return
	 */
	List<Comment> listForPublicationAndDomId(Long chapterId, String domId);

}
