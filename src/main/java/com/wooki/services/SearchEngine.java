package com.wooki.services;

import java.util.List;

import com.wooki.domain.model.Book;


/**
 * Provide method to implements search method on wookis resources. So the user
 * can provide an implementation in function of its environment and the indexing
 * tools available.
 * 
 * @author ccordenier
 * 
 */
public interface SearchEngine {

	/**
	 * List the books 
	 * @param queryString
	 * @return
	 */
	public List<Book> findBook(String queryString);
	
}
