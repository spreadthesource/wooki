package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
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
	private Block presBlock;

	@Inject
	private Block userBlock;

	@Property
	private List<FreshStuff> freshStuffs;

	@Property
	private FreshStuff current;

	@Property
	private List<Book> userBooks;

	@Property
	private Book currentBook;

	@Property
	private String username;

	@OnEvent(value = EventConstants.ACTIVATE)
	public boolean setupListBook() {
		// TODO Implement book list for authenticated user
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
		this.bookManager = (BookManager) applicationContext
				.getBean("bookManager");
		this.userBooks = bookManager.listByAuthor(username);
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

	public Block getUserCtx() {
		if (this.username == null) {
			return presBlock;
		} else {
			return userBlock;
		}
	}

}
