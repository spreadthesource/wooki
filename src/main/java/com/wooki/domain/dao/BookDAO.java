package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Book;

/**
 * DAO in charge of the Book
 * 
 * @author ccordenier
 * 
 */
public interface BookDAO extends GenericDAO<Book, Long>{

	/**
	 * 
	 * @param title
	 * @return
	 */
	Book findBookBySlugTitle(String title);

	/**
	 * List book with similar title.
	 *
	 * @param title
	 * @return
	 */
	List<Book> listByTitle(String title);
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	List<Book> listByAuthor(Long id);
	
	/**
	 * Verify if an author owns a book.
	 *
	 * @return
	 */
	boolean verifyBookOwner(Long bookId, String username);
}
