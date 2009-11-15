package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;

public class CommentDAOImpl extends HibernateDaoSupport implements CommentDAO {

	public Comment add(Comment comment) {
		if (comment == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		try {
			getHibernateTemplate().saveOrUpdate(comment);
			return comment;
		} catch (DataAccessException daEx) {
			logger.error(daEx);
			return null;
		}
	}

	public void delete(Comment comment) {
		if (comment == null) {
			throw new IllegalArgumentException("Chapter cannot be null.");
		}
		getHibernateTemplate().delete(comment);
	}

	public List<Comment> listForChapter(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = session.createCriteria(Comment.class);
		criteria.add(Restrictions.eq("chapter.id", chapterId));
		List<Comment> list = (List<Comment>) criteria.list();
		return list;
	}

	public List<Comment> listOpenForChapter(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = session.createCriteria(Comment.class);
		criteria.add(Restrictions.eq("chapter.id", chapterId));
		criteria.add(Restrictions.eq("state", CommentState.OPEN));

		List<Comment> list = (List<Comment>) criteria.list();
		return list;
	}

	public List<Comment> listForBook(Long book) {
		if (book == null) {
			throw new IllegalArgumentException("Book id cannot be null.");
		}
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria criteria = session.createCriteria(Comment.class);
		criteria.add(Restrictions.eq("chapter.book.id", book));
		List<Comment> list = (List<Comment>) criteria.list();
		return list;
	}

	public void update(Comment comment) {
		if (comment == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		try {
			getHibernateTemplate().update(comment);
		} catch (DataAccessException daEx) {
			logger.error(daEx);
		}
	}

}
