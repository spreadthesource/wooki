package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.SearchEngine;

/**
 * This page search and display a list of wooki elements in function of the
 * activation String.
 * 
 * @author ccordenier
 * 
 */
public class SearchResult {

	@Inject
	private SearchEngine searchService;
	
	@Property
	@Validate("regexp=[^%]*")
	private String queryString;

	@Property
	private List<Book> results;

	@Property
	private Book currentBook;

	@Property
	private User currentAuthor;

	@Property
	private int loopIdx;
	
	/**
	 * Prepare search
	 * 
	 * @param searchField
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public void search(String queryString) {
		this.queryString = queryString;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public String retrieveQuery() {
		return this.queryString;
	}

	@SetupRender
	public void setupRender() {
		this.results = searchService.findBook(queryString);
	}
	
}
