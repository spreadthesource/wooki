package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

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
	public void showCoAuthorsFeed() {
		this.showYours = false;
	}

	@OnEvent(value = "showUser")
	public void showUserFeed() {
		this.showYours = true;
	}

	public Object getFeed() {
		if (this.showYours) {
			return this.yourActivity;
		}
		return this.coAuthor;
	}

	public String getCoAuthorsClass() {
		if(this.showYours) {
			return "active";
		}
		return "inactive";
	}

	public String getUserClass() {
		if(this.showYours) {
			return "inactive";
		}
		return "active";

	}
	
}
