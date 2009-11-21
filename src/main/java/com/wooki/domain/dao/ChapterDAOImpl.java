package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Chapter;

@Repository("chapterDao")
public class ChapterDAOImpl extends GenericDAOImpl<Chapter, Long> implements
		ChapterDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public String getContent(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}

		try {
			String result = (String) getSession().getNamedQuery(
					"com.wooki.domain.model.chapter.getConent").setParameter(
					"id", chapter.getId()).uniqueResult();
			return result;
		} catch (RuntimeException ex) {
			logger.error(String.format(
					"Error while getting content content for chapter %s",
					chapter), ex);
			return null;
		}
	}

	public List<Chapter> listChapters(Long idBook) {
		if (idBook == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		try {
			List<Chapter> result = (List<Chapter>) getSession().getNamedQuery(
					"com.wooki.domain.model.chapter.listChapterForBook")
					.setParameter("book", idBook).list();
			return result;
		} catch (RuntimeException ex) {
			logger.error(String.format(
					"Error while listing chapters for book %d", idBook), ex);
			return null;
		}
	}

	public List<Chapter> listLastModified(Long id, int nbElts) {
		if (id == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		try {
			Criteria criteria = getSession().createCriteria(Chapter.class);
			criteria.add(Restrictions.eq("book.id", id));
			criteria.addOrder(Order.asc("lastModified"));
			return criteria.list();
		} catch (RuntimeException ex) {
			logger.error("Error while listing last modified chapters", ex);
			return null;
		}
	}

}
