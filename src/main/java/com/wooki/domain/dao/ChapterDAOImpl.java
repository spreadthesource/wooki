package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Chapter;

@Repository("chapterDao")
public class ChapterDAOImpl extends GenericDAOImpl<Chapter, Long> implements
		ChapterDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public String getContent(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Query query = this.entityManager.createQuery("select c.content from "
				+ getEntityType() + " c where c.id=:id");
		query.setParameter("id", chapterId);
		byte[] result = (byte[]) query.getSingleResult();
		if (result == null) {
			return "";
		}
		return new String(result);
	}

	public List<Chapter> listChapterInfo(Long bookId) {
		if (bookId == null) {
			throw new IllegalArgumentException(
					"Book id should not be null while lis chapters informations");
		}
		Query query = entityManager
				.createQuery(String
						.format(
								"select NEW %s(c.id, c.title, c.lastModified) from %s c where c.book.id=:book and c.deletionDate is null",
								getEntityType(), getEntityType()));
		query.setParameter("book", bookId);
		return query.getResultList();
	}

	public List<Chapter> listChapters(Long idBook) {
		if (idBook == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " c where c.book.id=:book and c.deletionDate is null");
		List<Chapter> result = (List<Chapter>) query.setParameter("book",
				idBook).getResultList();
		return result;
	}

	public List<Chapter> listLastModified(Long id, int nbElts) {
		if (id == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Query query = this.entityManager.createQuery("from "
				+ this.getEntityType()
				+ " c where c.book.id=:booId and c.deletionDate is null order by c.lastModified desc");
		query.setParameter("bookId", id);
		return query.getResultList();
	}

}
