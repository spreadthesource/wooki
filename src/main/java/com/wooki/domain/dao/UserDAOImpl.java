package com.wooki.domain.dao;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.User;

@Repository("userDao")
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

	private Logger logger = LoggerFactory.getLogger(UserDAO.class);

	public User findByUsername(String username) {
		if(username == null) {
			throw new IllegalArgumentException("Username should not be null to find it by name.");
		}
		Query query = this.entityManager.createQuery("from "
				+ this.getEntityType() + " u where lower(u.username)=:un");
		query.setParameter("un", username.toLowerCase());
		try {
			return (User) query.getSingleResult();
		} catch (RuntimeException re) {
			return null;
		}
	}

}
