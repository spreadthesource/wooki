package com.wooki.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Activity;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.EventType;
import com.wooki.domain.model.User;
import com.wooki.services.utils.SlugBuilder;

/**
 * Global wooki application business manager.
 * 
 * @author ccordenier
 * 
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("bookManager")
public class BookManagerImpl implements BookManager {

	@Autowired
	private BookDAO bookDao;

	@Autowired
	private UserDAO authorDao;

	@Autowired
	private ActivityDAO activityDao;

	@Transactional(readOnly = false)
	public void addAuthor(Book book, String username) {
		if (book == null || username == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}

		User toAdd = authorDao.findByUsername(username);

		if (toAdd == null) {
			return;
		}

		Book toUpdate = bookDao.findById(book.getId());
		toUpdate.addUser(toAdd);
		bookDao.update(toUpdate);
	}

	@Transactional(readOnly = false, rollbackFor = AuthorizationException.class)
	public Chapter addChapter(Book book, String title, String username)
			throws AuthorizationException {
		if (book == null || title == null || username == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}

		if (!bookDao.verifyBookOwner(book.getId(), username)) {
			throw new AuthorizationException();
		}

		// Create the new Chapter
		Chapter chapter = new Chapter();
		chapter.setTitle(title);
		chapter.setSlugTitle(SlugBuilder.buildSlug(title));
		Date creationDate = new Date();
		chapter.setCreationDate(creationDate);
		chapter.setLastModifed(new Timestamp(System.currentTimeMillis()));

		// Get managed entity to update
		Book toUpdate = bookDao.findById(book.getId());
		toUpdate.addChapter(chapter);
		chapter.setBook(toUpdate);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(username);
		activity.setBookId(toUpdate.getId());
		activity.setChapterId(chapter.getId());
		activity.setEventDate(creationDate);
		activity.setType(EventType.UPDATE);
		activity.setBookTitle(book.getTitle());
		activityDao.create(activity);

		return chapter;
	}

	@Transactional(readOnly = false)
	public Book create(String title, String author) {
		Book book = new Book();

		// Set basic properties
		book.setTitle(title);
		book.setSlugTitle(SlugBuilder.buildSlug(title));
		Date creationDate = new Date();
		book.setCreationDate(creationDate);
		book.setLastModified(creationDate);

		// Add abstract
		Chapter bookAbstract = new Chapter();
		bookAbstract.setTitle("Abstract");
		bookAbstract.setSlugTitle("Abstract");
		book.addChapter(bookAbstract);
		book.addUser(authorDao.findByUsername(author));
		bookAbstract.setBook(book);

		bookDao.create(book);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(author);
		activity.setBookId(book.getId());
		activity.setEventDate(creationDate);
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

	public void setBookDao(BookDAO bookDao) {
		this.bookDao = bookDao;
	}

	public void setAuthorDao(UserDAO authorDao) {
		this.authorDao = authorDao;
	}

	public void setActivityDao(ActivityDAO activityDao) {
		this.activityDao = activityDao;
	}

}
