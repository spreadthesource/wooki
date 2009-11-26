package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Book;

@Repository("bookDao")
public class BookDAOImpl extends GenericDAOImpl<Book, Long> implements BookDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public Book findBookBySlugTitle(String title) {
		Book result = (Book) getSession().getNamedQuery(
				"com.wooki.domain.model.book.findBySlugTitle").setParameter(
				"st", title).uniqueResult();
		return result;
	}

	public List<Book> listByAuthor(Long id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Author id cannot be null while listing book.");
		}
		Query query = entityManager.createQuery(String.format(
				"select b from %s b join b.users u where u.id=:uid", Book.class.getName()));
		query.setParameter("uid", id);
		return query.getResultList();
	}

	public List<Book> listByTitle(String title) {
		try {
			Criteria criteria = getSession().createCriteria(Book.class);
			criteria.add(Restrictions.like("title", "%" + title + "%"));
			return criteria.list();
		} catch (DataAccessException daEx) {
			logger.error(daEx.getMessage());
			return null;
		}
	}

	public boolean verifyBookOwner(Long bookId, String username) {
		if (bookId == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		try {
			Object result = getSession().getNamedQuery(
					"com.wooki.domain.model.book.verifyBookOwner")
					.setParameter("un", username).setParameter("id", bookId)
					.uniqueResult();
			return result != null;
		} catch (RuntimeException ex) {
			logger.debug("Error during owner verification", ex);
			return false;
		}
	}
}
