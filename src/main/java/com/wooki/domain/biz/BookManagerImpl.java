package com.wooki.domain.biz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Activity;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.EventType;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.utils.SlugBuilder;

/**
 * Global wooki application business manager.
 * 
 * @author ccordenier
 * 
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("bookManager")
public class BookManagerImpl implements BookManager {

	@Autowired
	private BookDAO bookDao;

	@Autowired
	private UserDAO authorDao;

	@Autowired
	private ActivityDAO activityDao;

	@Autowired
	private ChapterDAO chapterDao;

	@Autowired
	private WookiSecurityContext securityCtx;

	@Transactional(readOnly = false)
	public User addAuthor(Book book, String username)
			throws UserNotFoundException, UserAlreadyOwnerException {
		if (book == null || username == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}

		User toAdd = authorDao.findByUsername(username);

		if (toAdd == null) {
			throw new UserNotFoundException(username + " does not exist.");
		}

		if (bookDao.isOwner(book.getId(), username)) {
			throw new UserAlreadyOwnerException(username
					+ "is already an author the book");
		}

		book.addUser(toAdd);
		bookDao.update(book);
		return toAdd;
	}

	@Transactional(readOnly = false)
	public void removeAuthor(Book book, Long authorId) {
		if (book == null || authorId == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}
		User user = authorDao.findById(authorId);
		user.getBooks().remove(book);
		book.getAuthors().remove(user);
		bookDao.update(book);
	}

	@Transactional(readOnly = false)
	public Book updateTitle(Book book) throws TitleAlreadyInUseException {
		if (book == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}
		// Create slug title
		String slug = SlugBuilder.buildSlug(book.getTitle());
		
		// If book has changed of title
		if(book.getSlugTitle() != null && !book.getSlugTitle().equalsIgnoreCase(slug)) {
			Book result = bookDao.findBookBySlugTitle(slug);
			if(result != null) {
				throw new TitleAlreadyInUseException();
			}
		}
		
		if (!book.getSlugTitle().equals(slug)) {
			book.setSlugTitle(slug);
		}
		return bookDao.update(book);
	}

	public boolean isAuthor(Book book, String username) {
		if (book == null || username == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}
		return bookDao.isOwner(book.getId(), username);
	}

	@Transactional(readOnly = false, rollbackFor = AuthorizationException.class)
	public Chapter addChapter(Book book, String title)
			throws AuthorizationException {
		if (book == null || title == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}

		if (!securityCtx.isAuthorOfBook(book.getId())) {
			throw new AuthorizationException(
					"Current user is not an author of " + book.getTitle());
		}

		User author = securityCtx.getAuthor();

		// Create the new Chapter
		Chapter chapter = new Chapter();
		chapter.setTitle(title);
		chapter.setSlugTitle(SlugBuilder.buildSlug(title));
		Date creationDate = Calendar.getInstance().getTime();
		chapter.setCreationDate(creationDate);
		chapter.setLastModified(creationDate);

		// Get managed entity to update
		book = bookDao.findById(book.getId());
		book.addChapter(chapter);
		chapter.setBook(book);
		this.chapterDao.create(chapter);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(author.getUsername());
		activity.setBookId(book.getId());
		activity.setChapterId(chapter.getId());
		activity.setCreationDate(creationDate);
		activity.setType(EventType.UPDATE);
		activity.setBookTitle(book.getTitle());
		activityDao.create(activity);

		return chapter;
	}

	@Transactional(readOnly = false)
	public Book create(String title) {

		User author = securityCtx.getAuthor();

		if (author == null) {
			throw new AuthorizationException();
		}

		Book book = new Book();

		// Set basic properties
		book.setTitle(title);
		book.setSlugTitle(SlugBuilder.buildSlug(title));
		Date creationDate = new Date();
		book.setCreationDate(creationDate);
		book.setLastModified(creationDate);

		// Add abstract
		Chapter bookAbstract = new Chapter();
		bookAbstract.setCreationDate(creationDate);
		bookAbstract.setTitle("Abstract");
		bookAbstract.setSlugTitle("Abstract");
		book.addChapter(bookAbstract);
		book.addUser(author);
		bookAbstract.setBook(book);

		bookDao.create(book);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(author.getUsername());
		activity.setBookId(book.getId());
		activity.setCreationDate(creationDate);
		activity.setType(EventType.CREATE);
		activity.setBookTitle(book.getTitle());
		activityDao.create(activity);

		return book;
	}

	public Chapter getBookAbstract(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book parameter cannot be null");
		}
		Book upToDate = bookDao.findById(book.getId());
		if (book != null) {
			return upToDate.getChapters().get(0);
		}
		return null;
	}

	public Book findBookBySlugTitle(String title) {
		return bookDao.findBookBySlugTitle(title);
	}

	public Book findById(Long id) {
		return bookDao.findById(id);
	}

	public List<Book> list() {
		return bookDao.listAll();
	}

	public List<Book> listByTitle(String title) {
		return bookDao.listByTitle(title);
	}

	public List<Book> listByUser(String userName) {
		User author = authorDao.findByUsername(userName);
		if (author != null) {
			return bookDao.listByAuthor(author.getId());
		}
		return null;
	}

}
