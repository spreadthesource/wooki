package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Activity;

@Repository("activityDao")
public class ActivityDAOImpl extends GenericDAOImpl<Activity, Long> implements
		ActivityDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public List<Activity> list(int nbElements) {
		try {
			Criteria crit = getSession().createCriteria(Activity.class);
			crit.addOrder(Order.desc("eventDate"));
			crit.setMaxResults(nbElements);
			return crit.list();
		} catch (RuntimeException ex) {
			logger.error("Error during listing activites.", ex);
			return null;
		}
	}

	public List<Activity> listForBook(Long bookId, int nbElements) {
		try {
			Criteria crit = getSession().createCriteria(Activity.class);
			crit.add(Restrictions.eq("bookId", bookId));
			crit.addOrder(Order.desc("eventDate"));
			crit.setMaxResults(nbElements);
			return crit.list();
		} catch (RuntimeException ex) {
			logger.error(String.format(
					"Error during listing activites for book %d", bookId), ex);
			return null;
		}
	}

	public List<Activity> listByChapter(Long chapterId) {
		try {
			Criteria crit = getSession().createCriteria(Activity.class);
			crit.add(Restrictions.eq("chapterId", chapterId));
			return crit.list();
		} catch (RuntimeException ex) {
			logger.error(String.format(
					"Error during listing activities for chapter", chapterId),
					ex);
			return null;
		}
	}

}
