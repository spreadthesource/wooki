package com.wooki.services.security;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.model.User;

/**
 * Implement wooki security context in a web context.
 * 
 * @author ccordenier
 * 
 */
public class WookiSecurityContextImpl implements WookiSecurityContext {

	private CommentDAO commentDao;

	private BookDAO bookDAO;

	private UserDAO userDao;

	public void log(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		UsernamePasswordAuthenticationToken logged = new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(logged);
	}

	public User getAuthor() {
		String username = this.getUsername();
		if (username != null) {
			return userDao.findByUsername(username);
		}
		return null;
	}

	public boolean isAuthorOfBook(Long bookId) {
		String username = this.getUsername();
		if (username != null) {
			return bookDAO.isOwner(bookId, username);
		}
		return false;
	}

	public boolean isLoggedIn() {
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() != null) {
			return SecurityContextHolder.getContext().getAuthentication()
					.isAuthenticated();
		}
		return false;
	}

	public boolean isAuthorOfComment(Long commentId) {
		String username = this.getUsername();
		if (username != null) {
			return commentDao.isOwner(commentId, username);
		}
		return false;
	}

	private String getUsername() {
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() != null) {
			if (SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal() instanceof User) {
				return ((User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal()).getUsername();
			} else {
				return null;
			}
		}
		return null;
	}

	public BookDAO getBookDAO() {
		return bookDAO;
	}

	public void setBookDAO(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	public CommentDAO getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(CommentDAO commentDao) {
		this.commentDao = commentDao;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

}