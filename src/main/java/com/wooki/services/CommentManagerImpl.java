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
		// TODO Aggregate for all publication.
		return null;
	}

	public List<Comment> listOpenForPublication(Long chapterId) {
		return commentDao.listOpenForPublication(chapterId);
	}

	public List<Object[]> listCommentInfos(Long publicationId) {
		return commentDao.listCommentsInforForPublication(publicationId);
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
