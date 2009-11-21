package com.wooki.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.AuthorDAO;
import com.wooki.domain.exception.AuthorAlreadyException;
import com.wooki.domain.model.Author;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("authorManager")
public class AuthorManagerImpl implements AuthorManager {

	@Autowired
	private AuthorDAO authorDao;

	@Transactional(readOnly = false)
	public void addAuthor(Author author) throws AuthorAlreadyException {
		if (findByUsername(author.getUsername()) != null) {
			throw new AuthorAlreadyException();
		}
		// Encode password into database
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String pass = author.getPassword();
		author.setPassword(encoder.encodePassword(pass, WookiModule.SALT));
		authorDao.create(author);
	}

	public Author findByUsername(String username) {
		return authorDao.findByUsername(username);
	}

}
