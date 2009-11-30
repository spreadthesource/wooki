package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Publication;

@Repository("publicationDao")
public class PublicationDAOImpl extends GenericDAOImpl<Publication, Long>
		implements PublicationDAO {

	private Logger logger = LoggerFactory.getLogger(PublicationDAO.class);

	public Publication findLastRevision(Long chapterId) {
		Query query = entityManager.createQuery("from " + getEntityType() + " p where chapter.id=:id order by p.creationDate desc");
		query.setParameter("id", chapterId);
		query.setMaxResults(1);
		List<Publication> published = query.getResultList();
		if (published != null && published.size() > 0) {
			return published.get(0);
		} else {
			return null;
		}
	}

}
