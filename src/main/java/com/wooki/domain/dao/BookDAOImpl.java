package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wooki.domain.model.Book;

public class BookDAOImpl extends HibernateDaoSupport implements BookDAO {

	public Book add(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		try {
			getHibernateTemplate().saveOrUpdate(book);
			return book;
		} catch (DataAccessException daEx) {
			logger.debug(daEx);
			return null;
		}
	}

	public void delete(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book to delete cannot be null.");
		}
		getHibernateTemplate().delete(book);
	}

	public Book findById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null in findById");
		}
		return (Book) getHibernateTemplate().get(Book.class, id);
	}

	public Book findBookBySlugTitle(String title) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Book result = (Book) session.getNamedQuery(
				"com.wooki.domain.model.book.findBySlugTitle").setParameter(
				"st", title).uniqueResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Book> listAll() {
		List<Book> result = (List<Book>) getHibernateTemplate().loadAll(
				Book.class);
		return result;
	}

	public List<Book> listByAuthor(Long id) {
		return null;
	}

	public List<Book> listByTitle(String title) {
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.getCurrentSession();
			Criteria criteria = session.createCriteria(Book.class);
			criteria.add(Restrictions.like("title", "%" + title + "%"));
			return criteria.list();
		} catch (DataAccessException daEx) {
			logger.error(daEx.getMessage());
			return null;
		}
	}

	public void update(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("The book cannot be null");
		}
		getHibernateTemplate().update(book);
	}

}
