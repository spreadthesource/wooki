package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Book;

/**
 * DAO in charge of the Book
 * 
 * @author ccordenier
 * 
 */
public interface BookDAO {

	/**
	 * Add a newly created book.
	 * 
	 * @param book
	 */
	Book add(Book book);

	/**
	 * Delete a book.
	 *
	 * @param book
	 */
	void delete(Book book);

	/**
	 * Return the book for an id.
	 * 
	 * @param id
	 * @return
	 */
	Book findById(Long id);

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
	 * Return the book list.
	 *
	 * @return
	 */
	List<Book> listAll();

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	List<Book> listByAuthor(Long id);
	
	/**
	 * Update the given book.
	 *
	 * @param book
	 */
	void update(Book book);

	/**
	 * Verify if an author owns a book.
	 *
	 * @return
	 */
	boolean verifyBookOwner(Long bookId, String username);
}
