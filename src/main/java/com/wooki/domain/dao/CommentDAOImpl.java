package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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
		try {
			Criteria criteria = getSession().createCriteria(Comment.class);
			criteria.add(Restrictions.eq("publication.id", chapterId));
			criteria.add(Restrictions.eq("state", CommentState.OPEN));

			List<Comment> list = (List<Comment>) criteria.list();
			return list;
		} catch (RuntimeException ex) {
			logger.error(
					String.format(
							"Error while list open comments for chapter %d",
							chapterId), ex);
			return null;
		}
	}

	public List<Object[]> listCommentsInforForPublication(Long publicationId) {
		if (publicationId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		String queryStr = String
				.format(
						"select c.domId, count(c.domId) from %s c where c.publication.id=:id group by c.domId",
						Comment.class.getName());
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("id", publicationId);
		return query.getResultList();
	}

}
