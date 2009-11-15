package com.wooki.services;

import java.util.List;

import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.model.Comment;

public class CommentManagerImpl implements CommentManager {

	private CommentDAO commentDao;

	public List<Comment> listAll(Long bookId) {
		if (bookId == null) {
			throw new IllegalArgumentException("BookId cannot be null");
		}
		return commentDao.listForBook(bookId);
	}

	public List<Comment> listOpenForBook(Long bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Comment> listOpenForChapter(Long chapterId) {
		return commentDao.listOpenForChapter(chapterId);
	}

	public void refuseComment(Comment comment) {
		// TODO Auto-generated method stub

	}

	public void solveComment(Comment comment) {
		// TODO Auto-generated method stub

	}

	public void setCommentDao(CommentDAO commentDao) {
		this.commentDao = commentDao;
	}

}
