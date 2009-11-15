package com.wooki.services;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.model.Author;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

/**
 * Manager interface to manipulate DAO Interaction.
 * 
 * @author ccordenier
 * 
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false)
	Book create(String title, Author author);

	/**
	 * Add a chapter to a given book.
	 * 
	 * @param book
	 *            Book must exists in DB before a call to this method.
	 * @param title
	 *            The tile for the new chapter.
	 * @param username TODO
	 */
	@Transactional(readOnly = false)
	Chapter addChapter(Book book, String title, String username);

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
