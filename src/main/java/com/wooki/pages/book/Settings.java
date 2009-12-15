package com.wooki.pages.book;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.WookiEventConstants;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.utils.SlugBuilder;

/**
 * Update settings of a given book
 */
public class Settings {

	@Inject
	private WookiSecurityContext securityCtx;

	@Inject
	private BookManager bookManager;

	@Inject
	private UserManager userManager;

	@Inject
	private Block authorRow;

	@InjectComponent
	private Form addAuthorForm;

	@InjectComponent
	private Form bookInfoForm;

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
	@Persist
	private int rowIndex;

	private User loggedAuthor;

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
		this.rowIndex = 0;
		this.loggedAuthor = securityCtx.getAuthor();

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

	@OnEvent(value = EventConstants.VALIDATE, component = "title")
	public void checkTitle(String title) {
		if (!book.getSlugTitle().equalsIgnoreCase(SlugBuilder.buildSlug(title))) {
			Book result = bookManager.findBookBySlugTitle(SlugBuilder
					.buildSlug(title));
			if (result != null) {
				bookInfoForm.recordError("Title is already in use");
			}
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "bookInfoForm")
	public void updateBook() {
		try {
			this.book = bookManager.updateTitle(book);
		} catch (TitleAlreadyInUseException taiuEx) {
			bookInfoForm.recordError("Title is already in use");
		}
	}

	@OnEvent(value = WookiEventConstants.REMOVE)
	public void removeAuthor(Long authorId) {
		bookManager.removeAuthor(this.book, authorId);
	}

	@OnEvent(value = EventConstants.PROVIDE_COMPLETIONS)
	public String[] provideAuthorList(String prefix) {
		return userManager.listUserNames(prefix);
	}

	public boolean isLoggedAuthor() {
		return this.currentAuthor.getUsername().equalsIgnoreCase(
				this.loggedAuthor.getUsername());
	}

}
