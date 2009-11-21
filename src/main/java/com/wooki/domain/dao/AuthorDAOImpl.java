package com.wooki.domain.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Author;

@Repository("authorDao")
public class AuthorDAOImpl extends GenericDAOImpl<Author, Long> implements AuthorDAO {

	private Logger logger = LoggerFactory.getLogger(AuthorDAO.class);

	public Author findByUsername(String username) {
		try {
			Criteria crit = getSession().createCriteria(Author.class);
			crit.add(Restrictions.ilike("username", username));
			return (Author) crit.uniqueResult();
		} catch (RuntimeException daEx) {
			logger.error(String.format("Error while searching for author %s",
					username));
			return null;
		}
	}

}
