package com.wooki.services;

import java.util.List;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

/**
 * Manager interface to manipulate DAO Interaction.
 * 
 * @author ccordenier
 * 
 */
public interface BookManager {

	/**
	 * Create a new book instance with basic properties initialized.
	 * 
	 * @param title
	 *            The title of the book
	 * @param author
	 *            Author must exist before a call to this method.
	 * @return
	 */
	Book create(String title, String author);

	/**
	 * Add an author to a given book, author must exist before calling this
	 * method.
	 * 
	 * @param book
	 *            Book must exists in DB before a call to this method.
	 * @param title
	 *            The tile for the new chapter.
	 * @param username
	 *            TODO
	 */
	void addAuthor(Book book, String username);

	/**
	 * Add a chapter to a given book.
	 * 
	 * @param book
	 *            Book must exists in DB before a call to this method.
	 * @param title
	 *            The tile for the new chapter.
	 * @param username
	 *            TODO
	 */
	Chapter addChapter(Book book, String title, String username);

	/**
	 * Get the book abstract chapter (first item in the list)
	 * 
	 * @param bookId
	 * @return
	 */
	Chapter getBookAbstract(Book book);

	/**
	 * Get a book from it short name.
	 * 
	 * @param title
	 * @return
	 */
	Book findBookBySlugTitle(String title);

	/**
	 * Return the list of existing books.
	 * 
	 * @return
	 */
	List<Book> list();

	/**
	 * List the books of an author.
	 * 
	 * @param userName
	 * @return
	 */
	List<Book> listByAuthor(String userName);

	/**
	 * Find all the books matching a title.
	 * 
	 * @param title
	 * @return
	 */
	public List<Book> listByTitle(String title);

}
