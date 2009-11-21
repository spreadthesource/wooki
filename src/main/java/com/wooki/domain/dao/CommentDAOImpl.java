package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;

@Repository("commentDao")
public class CommentDAOImpl extends GenericDAOImpl<Comment, Long> implements
		CommentDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public List<Comment> listForChapter(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		try {
			Criteria criteria = getSession().createCriteria(Comment.class);
			criteria.add(Restrictions.eq("chapter.id", chapterId));
			List<Comment> list = (List<Comment>) criteria.list();
			return list;
		} catch (Exception ex) {
			logger.error(String.format(
					"Error while listing comments for chapter %d", chapterId),
					ex);
			return null;
		}
	}

	public List<Comment> listOpenForChapter(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		try {
			Criteria criteria = getSession().createCriteria(Comment.class);
			criteria.add(Restrictions.eq("chapter.id", chapterId));
			criteria.add(Restrictions.eq("state", CommentState.OPEN));

			List<Comment> list = (List<Comment>) criteria.list();
			return list;
		} catch (RuntimeException ex) {
			logger.error(
					String.format(
							"Error while list open comments for chapter %d",
							chapterId), ex);
			return null;
		}
	}

	public List<Comment> listForBook(Long book) {
		if (book == null) {
			throw new IllegalArgumentException("Book id cannot be null.");
		}
		try {
			Criteria criteria = getSession().createCriteria(Comment.class);
			criteria.add(Restrictions.eq("chapter.book.id", book));
			List<Comment> list = (List<Comment>) criteria.list();
			return list;
		} catch (RuntimeException ex) {
			logger.error(String.format(
					"Error while listing comments for book %d", book), ex);
			return null;
		}
	}

}
