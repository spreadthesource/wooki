package com.wooki.domain.biz;

import java.util.List;

import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.User;

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
	 * @return
	 */
	Book create(String title);

	/**
	 * Update book.
	 *
	 * @param book
	 * @return
	 */
	Book updateTitle(Book book) throws TitleAlreadyInUseException;

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
	 * @return TODO
	 */
	User addAuthor(Book book, String username) throws UserNotFoundException, UserAlreadyOwnerException ;
	
	/**
	 * Remove an author from a book.
	 */
	void removeAuthor(Book book, Long authorId);

	/**
	 * Check if the user is author of a book.
	 *
	 * @param book
	 * @param username
	 * @return
	 */
	boolean isAuthor(Book book, String username);
	
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
	Chapter addChapter(Book book, String title) throws AuthorizationException;

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
	Book findById(Long id);
	
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
	List<Book> listByUser(String userName);

	/**
	 * Find all the books matching a title.
	 * 
	 * @param title
	 * @return
	 */
	public List<Book> listByTitle(String title);

}
