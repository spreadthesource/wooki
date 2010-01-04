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

package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.WookiEventConstants;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Display an index page for wooki application. If no user logged in or
 * requested, then a default signup block will be displayed. If not, then the
 * requested user book list will be displayed.
 * 
 * @author ccordenier
 * 
 */
public class Dashboard {

	@Inject
	private BookManager bookManager;

	@Inject
	private WookiSecurityContext securityCtx;

	@Inject
	private Block coAuthor;

	@Inject
	private Block yourActivity;

	@InjectPage
	private com.wooki.pages.book.Index index;

	@Property
	private List<Book> userBooks;

	@Property
	private Book currentBook;

	@Property
	private User user;

	@Property
	@Persist
	private boolean showYours;

	@Property
	@Validate("required")
	private String bookTitle;

	@Persist(PersistenceConstants.FLASH)
	private boolean firstAccess;

	/**
	 * Set current user if someone has logged in.
	 * 
	 * @return
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public boolean setupListBook() {
		if (securityCtx.isLoggedIn()) {
			this.user = securityCtx.getAuthor();
			this.userBooks = bookManager.listByUser(user.getUsername());
			return true;
		}
		return false;
	}

	@SetupRender
	public Object setupDashboard() {
		if (this.user == null) {
			return false;
		}
		return true;
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "createBookForm")
	public Object createBook() {
		Book created = bookManager.create(bookTitle);
		index.setBookId(created.getId());
		return index;
	}

	@OnEvent(value = "showCoAuthors")
	public Object showCoAuthorsFeed() {
		this.showYours = false;
		return this.coAuthor;
	}

	@OnEvent(value = "showUser")
	public Object showUserFeed() {
		this.showYours = true;
		return this.yourActivity;
	}

	@OnEvent(value = WookiEventConstants.REMOVE, component = "removeBook")
	public void removeBook(Long bookId) {
		this.bookManager.remove(bookId);
	}

	public Object getFeed() {
		if (this.showYours) {
			return this.yourActivity;
		}
		return this.coAuthor;
	}

	public String getCoAuthorsClass() {
		if (this.showYours) {
			return "inactive";
		}
		return "active";
	}

	public String getUserClass() {
		if (this.showYours) {
			return "active";
		}
		return "inactive";

	}

	public boolean isFirstAccess() {
		return firstAccess;
	}

	public void setFirstAccess(boolean firstAccess) {
		this.firstAccess = firstAccess;
	}

}
