//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services.security;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.ChapterDAO;
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

	private ChapterDAO chapterDao;
	
	public void log(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		UsernamePasswordAuthenticationToken logged = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
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
			return bookDAO.isAuthor(bookId, username);
		}
		return false;
	}
	
	public boolean isAuthorOfChapter(Long chapterId) {
		String username = this.getUsername();
		if (username != null) {
			return chapterDao.isAuthor(chapterId, username);
		}
		return false;
	}

	public boolean isLoggedIn() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		}
		return false;
	}

	public boolean isOwnerOfBook(Long bookId) {
		String username = this.getUsername();
		if (username != null) {
			return bookDAO.isOwner(bookId, username);
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

	public String getUsername() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
				return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
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

	public ChapterDAO getChapterDao() {
		return chapterDao;
	}

	public void setChapterDao(ChapterDAO chapterDao) {
		this.chapterDao = chapterDao;
	}

}
