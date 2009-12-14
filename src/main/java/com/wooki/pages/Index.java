package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Activity;
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
public class Index {

	@Inject
	private ActivityManager activityManager;

	@Inject
	private BookManager bookManager;

	@Inject
	private WookiSecurityContext securityCtx;
	
	@Inject
	private Block presBlock;

	@Inject
	private Block userBlock;

	@InjectPage
	private com.wooki.pages.book.Index index;

	@Property
	private List<Activity> freshStuffs;

	@Property
	private Activity current;

	@Property
	private List<Book> userBooks;

	@Property
	private Book currentBook;

	private String username;

	@Property
	@Validate("required")
	private String bookTitle;

	private boolean logged;

	/**
	 * Set current user if someone has logged in.
	 * 
	 * @return
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public boolean setupListBook() {
		if(securityCtx.isLoggedIn()) {
			User user = securityCtx.getAuthor();
			this.username = user.getUsername();
			this.userBooks = bookManager.listByUser(username);
			return true;
		}
		return false;
	}

	/**
	 * If the user requested is not the user logged in, simply display his list
	 * of book.
	 * 
	 * @param username
	 * @return
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public boolean setupBookList(String username) {
		this.username = username;
		this.userBooks = bookManager.listByUser(username);
		return true;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public String getCurrentUser() {
		return username;
	}

	@SetupRender
	public void setupFreshStuff() {
		freshStuffs = activityManager.listAll(10);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "createBookForm")
	public Object createBook() {
		Book created = bookManager.create(bookTitle);
		index.setBookId(created.getId());
		return index;
	}

	public Block getUserCtx() {
		if (this.username == null) {
			return presBlock;
		} else {
			return userBlock;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
