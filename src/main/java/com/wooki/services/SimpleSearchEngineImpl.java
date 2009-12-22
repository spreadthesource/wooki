package com.wooki.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;

/**
 * Basic implementation of SearchService.
 * 
 * @author ccordenier
 * 
 */
public class SimpleSearchEngineImpl implements SearchEngine {

	@Autowired
	private BookManager bookManager;

	public List<Book> findBook(String queryString) {
		// Check empty query string
		if (queryString == null || "".equals(queryString.trim())) {
			return new ArrayList<Book>();
		}
		return bookManager.listByTitle(queryString);
	}

}
