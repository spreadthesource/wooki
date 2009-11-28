package com.wooki.pages;

import java.security.Principal;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.context.ApplicationContext;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.FreshStuff;
import com.wooki.services.ActivityManager;
import com.wooki.services.BookManager;

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
	private ApplicationContext applicationContext;

	private ActivityManager activityManager;

	private BookManager bookManager;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private Block presBlock;

	@Inject
	private Block userBlock;

	@InjectPage
	private com.wooki.pages.book.Index index;

	@Property
	private List<FreshStuff> freshStuffs;

	@Property
	private FreshStuff current;

	@Property
	private List<Book> userBooks;

	@Property
	private Book currentBook;

	private String username;

	@Property
	@Validate("required")
	private String bookTitle;
	
	private boolean logged;

	@PageAttached
	private void setupServices() {
		this.bookManager = (BookManager) applicationContext
				.getBean("bookManager");
	}

	/**
	 * Set current user if someone has logged in.
	 * 
	 * @return
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public boolean setupListBook() {
		Principal principal = requestGlobals.getHTTPServletRequest()
				.getUserPrincipal();
		if (principal != null && principal.getName() != "") {
			this.username = principal.getName();
			this.userBooks = bookManager.listByUser(username);
		}
		return true;
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
		activityManager = (ActivityManager) applicationContext
				.getBean("activityManager");
		freshStuffs = activityManager.listFreshStuff(10);
	}


	@OnEvent(value = EventConstants.SUCCESS, component = "createBookForm")
	public Object createBook() {
		Book created = bookManager.create(bookTitle, username);
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
