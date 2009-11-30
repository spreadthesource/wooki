package com.wooki.services.internal;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.model.User;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Inject
	private ApplicationContext applicationContext;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		UserManager authManager = (UserManager) applicationContext.getBean("userManager");
		User user = authManager.findByUsername(username);
		return user;
	}

}
