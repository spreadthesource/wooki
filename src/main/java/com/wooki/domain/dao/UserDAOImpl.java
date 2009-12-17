package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wooki.domain.model.User;

@Repository("userDao")
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

	public User findByUsername(String username) {
		if (username == null) {
			throw new IllegalArgumentException(
					"Username should not be null to find it by name.");
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

	public String[] listUserNames(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException(
					"Username should not be null to find it by name.");
		}
		Query query = this.entityManager.createQuery("select u.username from "
				+ this.getEntityType() + " u where lower(u.username) like :un");
		query.setParameter("un", prefix.toLowerCase() + "%");
		List<String> result = (List<String>) query.getResultList();
		if (result != null) {
			return result.toArray(new String[0]);
		}
		return new String[] {};
	}

}
