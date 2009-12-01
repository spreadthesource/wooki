package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;

@Repository("commentDao")
public class CommentDAOImpl extends GenericDAOImpl<Comment, Long> implements
		CommentDAO {

	private Logger logger = LoggerFactory.getLogger(ActivityDAO.class);

	public List<Comment> listOpenForPublication(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " c where c.publication.id=:pubId and c.state=:st");
		query.setParameter("pubId", chapterId);
		query.setParameter("st", CommentState.OPEN);
		return query.getResultList();
	}

	public List<Object[]> listCommentsInforForPublication(Long publicationId) {
		if (publicationId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		String queryStr = String
				.format(
						"select c.domId, count(c.domId) from %s c where c.publication.id=:id group by c.domId",
						Comment.class.getName());
		Query query = this.entityManager.createQuery(queryStr);
		query.setParameter("id", publicationId);
		return query.getResultList();
	}

}
