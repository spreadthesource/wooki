package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wooki.domain.model.Activity;

public class ActivityDAOImpl extends HibernateDaoSupport implements ActivityDAO {

	public Activity add(Activity activity) {
		if (activity == null) {
			throw new IllegalArgumentException("author cannot be null");
		}
		try {
			getHibernateTemplate().saveOrUpdate(activity);
			return activity;
		} catch (DataAccessException daEx) {
			logger.debug(daEx);
			return null;
		}
	}

	public List<Activity> list(int nbElements) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria crit = session.createCriteria(Activity.class);
		crit.addOrder(Order.desc("eventDate"));
		crit.setMaxResults(nbElements);
		return crit.list();
	}

	public List<Activity> listForBook(Long bookId, int nbElements) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria crit = session.createCriteria(Activity.class);
		crit.add(Restrictions.eq("bookId", bookId));
		crit.addOrder(Order.desc("eventDate"));
		crit.setMaxResults(nbElements);
		return crit.list();
	}

	public List<Activity> listByChapter(Long chapterId) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Criteria crit = session.createCriteria(Activity.class);
		crit.add(Restrictions.eq("chapterId", chapterId));
		return crit.list();
	}

}
