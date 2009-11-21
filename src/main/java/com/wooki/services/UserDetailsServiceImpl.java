package com.wooki.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.wooki.domain.model.Author;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Inject
	private ApplicationContext applicationContext;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		AuthorManager authManager = (AuthorManager) applicationContext.getBean("authorManager");
		Author author = authManager.findByUsername(username);
		return author;
	}

}
