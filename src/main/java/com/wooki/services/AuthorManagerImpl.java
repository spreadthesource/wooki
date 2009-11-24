package com.wooki.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("authorManager")
public class AuthorManagerImpl implements UserManager {

	@Autowired
	private UserDAO authorDao;

	@Transactional(readOnly = false)
	public void addUser(User author) throws UserAlreadyException {
		if (findByUsername(author.getUsername()) != null) {
			throw new UserAlreadyException();
		}
		// Encode password into database
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String pass = author.getPassword();
		author.setPassword(encoder.encodePassword(pass, WookiModule.SALT));
		authorDao.create(author);
	}

	public User findByUsername(String username) {
		return authorDao.findByUsername(username);
	}

}
