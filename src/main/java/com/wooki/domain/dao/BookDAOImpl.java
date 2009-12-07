package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Book;

@Repository("bookDao")
public class BookDAOImpl extends GenericDAOImpl<Book, Long> implements BookDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public Book findBookBySlugTitle(String title) {
		Query query = this.entityManager.createQuery("select b from "
				+ getEntityType() + " b where b.slugTitle=:st");
		return (Book) query.setParameter("st", title).getSingleResult();
	}

	public List<Book> listByAuthor(Long id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Author id cannot be null while listing book.");
		}
		Query query = entityManager.createQuery("select b from "
				+ getEntityType() + " b join b.users u where u.id=:uid and b.deletionDate is null");
		query.setParameter("uid", id);
		return (List<Book>) query.getResultList();
	}

	public List<Book> listByTitle(String title) {
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " b where b.title like :title and b.deletionDate is null");
		query.setParameter("title", title);
		return (List<Book>) query.getResultList();
	}

	public boolean isOwner(Long bookId, String username) {
		if (bookId == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		Query query = this.entityManager
				.createQuery("select count(b) from Book b join b.users as u where b.id=:id and u.username=:un and b.deletionDate is null");
		Long result = (Long) query.setParameter("un", username).setParameter(
				"id", bookId).getSingleResult();
		return result > 0;
	}
}
