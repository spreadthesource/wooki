package com.wooki.pages.book;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.WookiEventConstants;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.utils.SlugBuilder;

/**
 * Update settings of a given book
 */
public class Settings {

	@Inject
	private BookManager bookManager;

	@Inject
	private UserManager userManager;

	@Inject
	private Block authorRow;

	@InjectComponent
	private Form addAuthorForm;

	@Property
	private User currentAuthor;

	@Property
	private Book book;

	@Property
	private Long bookId;

	@Property
	private List<User> authors;

	@Property
	@Validate("required")
	private String newAuthor;

	@Property
	@Validate("required")
	private String title;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {

		// Check parameter numbers
		if (ctx.getCount() == 0) {
			return com.wooki.pages.Index.class;
		}

		this.bookId = ctx.get(Long.class, 0);

		// Get book related information
		this.book = bookManager.findById(bookId);

		if (this.book == null) {
			throw new IllegalArgumentException("Book does not exist.");
		}

		this.authors = this.book.getAuthors();

		return null;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long retrieveBookId() {
		return bookId;
	}

	@OnEvent(value = EventConstants.VALIDATE, component = "newAuthor")
	public void checkAuthor(String authorName) {
		User toAdd = userManager.findByUsername(authorName);
		if (toAdd == null) {
			addAuthorForm.recordError(String.format("User '%s' does not exist",
					authorName));
		} else {
			if (bookManager.isAuthor(book, authorName)) {
				addAuthorForm
						.recordError(String.format(
								"User '%s' is already author of the book.",
								authorName));
			}
		}

	}

	@OnEvent(value = EventConstants.SUCCESS, component = "addAuthorForm")
	public Object addAuthor() throws UserNotFoundException,
			UserAlreadyOwnerException {
		this.currentAuthor = bookManager.addAuthor(this.book, this.newAuthor);
		return authorRow;
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "bookInfoForm")
	public void updateBook() {
		this.book = bookManager.updateTitle(book);
	}

	@OnEvent(value = WookiEventConstants.REMOVE)
	public void removeAuthor(Long authorId) {
		bookManager.removeAuthor(this.book, authorId);
	}

	@OnEvent(value = EventConstants.PROVIDE_COMPLETIONS)
	public String[] provideAuthorList(String prefix) {
		return userManager.listUserNames(prefix);
	}

}
