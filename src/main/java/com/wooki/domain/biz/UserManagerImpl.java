package com.wooki.domain.biz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.services.WookiModule;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("userManager")
public class UserManagerImpl implements UserManager {

	@Autowired
	private UserDAO authorDao;

	@Transactional(readOnly = false, rollbackFor = UserAlreadyException.class)
	public void addUser(User author) throws UserAlreadyException {
		if (findByUsername(author.getUsername()) != null) {
			throw new UserAlreadyException();
		}
		// Encode password into database
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String pass = author.getPassword();
		author.setCreationDate(new Date());
		author.setPassword(encoder.encodePassword(pass, WookiModule.SALT));
		authorDao.create(author);
	}

	public User findByUsername(String username) {
		return authorDao.findByUsername(username);
	}

	public String[] listUserNames(String prefix) {
		return authorDao.listUserNames(prefix);
	}

}
