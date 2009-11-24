package com.wooki.domain.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Publication;

@Repository("publicationDao")
public class PublicationDAOImpl extends GenericDAOImpl<Publication, Long>
		implements PublicationDAO {

	private Logger logger = LoggerFactory.getLogger(PublicationDAO.class);

	public Publication findLastRevision(Long chapterId) {
		try {
			Criteria crit = getSession().createCriteria(Publication.class);
			crit.addOrder(Order.desc("revisionDate"));
			crit.add(Restrictions.eq("chapter.id", chapterId));
			List<Publication> published = crit.list();
			if (published != null && published.size() > 0) {
				return published.get(0);
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			logger.error(String.format(
					"Error while searching for last revision of %d chapter",
					chapterId), re);
			return null;
		}
	}

}
