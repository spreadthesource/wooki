package com.wooki.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.model.Comment;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("commentManager")
public class CommentManagerImpl implements CommentManager {

	@Autowired
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void refuseComment(Comment comment) {
		// TODO Auto-generated method stub

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void solveComment(Comment comment) {
		// TODO Auto-generated method stub

	}

	public void setCommentDao(CommentDAO commentDao) {
		this.commentDao = commentDao;
	}

}
