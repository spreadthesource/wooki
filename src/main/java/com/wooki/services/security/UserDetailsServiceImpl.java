package com.wooki.services.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.model.User;

/**
 * Custom implementation of use details service for spring security.
 *
 * @author ccordenier
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserManager userManager;
	
	public UserDetailsServiceImpl(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		User user = userManager.findByUsername(username);
		return user;
	}

}
