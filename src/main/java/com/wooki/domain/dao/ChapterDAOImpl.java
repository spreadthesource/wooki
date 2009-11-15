package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wooki.domain.model.Chapter;

public class ChapterDAOImpl extends HibernateDaoSupport implements ChapterDAO {

	public Chapter add(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		try {
			getHibernateTemplate().saveOrUpdate(chapter);
			return chapter;
		} catch (DataAccessException daEx) {
			logger.error(daEx);
			return null;
		}
	}

	public void delete(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Chapter cannot be null.");
		}
		getHibernateTemplate().delete(chapter);
	}

	public Chapter findById(Long id) {
		if (id != null) {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();
			Chapter result = (Chapter) session.load(Chapter.class, id);
			return result;
		} else {
			throw new IllegalArgumentException("Id cannot be null.");
		}
	}

	public String getContent(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		String result = (String) session.getNamedQuery(
				"com.wooki.domain.model.chapter.getConent").setParameter("id",
				chapter.getId()).uniqueResult();
		return result;
	}

	public List<Chapter> listChapters(Long idBook) {
		if (idBook == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		List<Chapter> result = (List<Chapter>) session.getNamedQuery(
				"com.wooki.domain.model.chapter.listChapterForBook")
				.setParameter("book", idBook).list();
		return result;
	}

	public List<Chapter> listLastModified(Long id, int nbElts) {
		if (id == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();
			Criteria criteria = session.createCriteria(Chapter.class);
			criteria.add(Restrictions.eq("book.id", id));
			criteria.addOrder(Order.asc("lastModified"));
			return criteria.list();
		} catch (DataAccessException daEx) {
			logger.error(daEx.getMessage());
			return null;
		}
	}

	public void update(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Chapter cannot be null.");
		}
		getHibernateTemplate().update(chapter);
	}

}
