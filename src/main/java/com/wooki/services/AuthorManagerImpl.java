package com.wooki.services;

import com.wooki.domain.dao.AuthorDAOImpl;
import com.wooki.domain.exception.AuthorAlreadyException;
import com.wooki.domain.model.Author;

public class AuthorManagerImpl implements AuthorManager {

	private AuthorDAOImpl authorDao;

	public Author addAuthor(Author author) throws AuthorAlreadyException {
		if(findByUsername(author.getUsername()) != null) {
			throw new AuthorAlreadyException();
		}
		return authorDao.add(author);
	}

	public boolean checkPassword(String username, String password) {
		return authorDao.checkPassword(username, password);
	}

	public Author findByUsername(String username) {
		return authorDao.findByUsername(username);
	}

	public void setAuthorDao(AuthorDAOImpl authorDao) {
		this.authorDao = authorDao;
	}

}
