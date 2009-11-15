package com.wooki.domain.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wooki.domain.model.Author;

public class AuthorDAOImpl extends HibernateDaoSupport implements AuthorDAO {

	public Author add(Author author) {
		if (author == null) {
			throw new IllegalArgumentException("author cannot be null");
		}
		try {
			getHibernateTemplate().saveOrUpdate(author);
			return author;
		} catch (DataAccessException daEx) {
			logger.debug(daEx);
			return null;
		}
	}

	public boolean checkPassword(String username, String password) {
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();
			Criteria crit = session.createCriteria(Author.class);
			crit.add(Restrictions.ilike("username", username));
			crit.add(Restrictions.eq("password", password));
			Author result = (Author) crit.uniqueResult();
			return result != null;
		} catch (DataAccessException daEx) {
			logger.debug(daEx);
			return false;
		}
	}

	public Author findByUsername(String username) {
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();
			Criteria crit = session.createCriteria(Author.class);
			crit.add(Restrictions.ilike("username", username));
			Author result = (Author) crit.uniqueResult();
			return result;
		} catch (DataAccessException daEx) {
			logger.debug(daEx);
			return null;
		}
	}

}
