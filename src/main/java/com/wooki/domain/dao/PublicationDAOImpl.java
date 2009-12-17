package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Publication;

@Repository("publicationDao")
public class PublicationDAOImpl extends GenericDAOImpl<Publication, Long> implements PublicationDAO {

	public Publication findLastRevision(Long chapterId) {
		Query query = entityManager.createQuery("from " + getEntityType() + " p where chapter.id=:id and p.deletionDate is null order by p.creationDate desc");
		query.setParameter("id", chapterId);
		query.setMaxResults(10);
		List<Publication> published = query.getResultList();
		if (published != null && published.size() > 0) {
			return published.get(0);
		} else {
			return null;
		}
	}

	public Publication findLastPublishedRevision(Long chapterId) {
		Query query = entityManager.createQuery("from " + getEntityType()
				+ " p where chapter.id=:id and p.deletionDate is null and p.published = 1 order by p.creationDate desc");
		query.setParameter("id", chapterId);
		query.setMaxResults(10);
		List<Publication> published = query.getResultList();
		if (published != null && published.size() > 0) {
			return published.get(0);
		} else {
			return null;
		}
	}

}
