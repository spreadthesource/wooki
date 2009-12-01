package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Activity;

@Repository("activityDao")
public class ActivityDAOImpl extends GenericDAOImpl<Activity, Long> implements
		ActivityDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public List<Activity> list(int nbElements) {
		Query query = entityManager.createQuery("from " + getEntityType()
				+ " a order by a.creationDate desc");
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listForBook(Long bookId, int nbElements) {
		Query query = entityManager.createQuery("from " + getEntityType()
				+ " a where a.bookId=:id order by a.creationDate desc");
		query.setMaxResults(nbElements);
		query.setParameter("id", bookId);
		return query.getResultList();
	}

	public List<Activity> listByChapter(Long chapterId) {
		Query query = entityManager.createQuery("from " + getEntityType()
				+ " a where a.chapterId=:id order by a.creationDate desc");
		query.setParameter("id", chapterId);
		return query.getResultList();
	}

}
