package com.wooki.domain.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.User;

@Repository("userDao")
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

	private Logger logger = LoggerFactory.getLogger(UserDAO.class);

	public User findByUsername(String username) {
		try {
			Criteria crit = getSession().createCriteria(User.class);
			crit.add(Restrictions.ilike("username", username));
			return (User) crit.uniqueResult();
		} catch (RuntimeException daEx) {
			logger.error(String.format("Error while searching for user %s",
					username));
			return null;
		}
	}

}
