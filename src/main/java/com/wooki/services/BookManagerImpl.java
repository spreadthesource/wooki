package com.wooki.services;

import java.util.Date;
import java.util.List;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.AuthorDAO;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Activity;
import com.wooki.domain.model.Author;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.EventType;
import com.wooki.services.utils.SlugBuilder;

/**
 * Global wooki application business manager.
 * 
 * @author ccordenier
 * 
 */
public class BookManagerImpl implements BookManager {

	private BookDAO bookDao;

	private AuthorDAO authorDao;

	private ActivityDAO activityDao;

	public void addAuthor(Book book, String username) {
		if (book == null || username == null) {
			throw new IllegalArgumentException(
					"Book and chapter title cannot be null for addition.");
		}

		Author toAdd = authorDao.findByUsername(username);

		if (toAdd == null) {
			return;
		}

		Book toUpdate = bookDao.findById(book.getId());
		toUpdate.addAuthor(toAdd);
		bookDao.update(toUpdate);
	}

	public Chapter addChapter(Book book, String title, String username) {
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
		chapter.setLastModifed(creationDate);

		Book toUpdate = bookDao.findById(book.getId());
		toUpdate.addChapter(chapter);
		chapter.setBook(toUpdate);
		bookDao.update(toUpdate);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(username);
		activity.setBookId(toUpdate.getId());
		activity.setChapterId(chapter.getId());
		activity.setEventDate(creationDate);
		activity.setType(EventType.UPDATE);
		activity.setBookTitle(book.getTitle());
		activityDao.add(activity);

		return chapter;
	}

	public Book create(String title, Author author) {
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
		book.addAuthor(author);
		bookAbstract.setBook(book);

		Book result = bookDao.add(book);

		// Add activity event
		Activity activity = new Activity();
		activity.setUsername(author.getUsername());
		activity.setBookId(result.getId());
		activity.setEventDate(creationDate);
		activity.setType(EventType.CREATE);
		activity.setBookTitle(result.getTitle());
		activityDao.add(activity);

		return result;
	}

	public Book findBookBySlugTitle(String title) {
		return bookDao.findBookBySlugTitle(title);
	}

	public List<Book> list() {
		return bookDao.listAll();
	}

	public List<Book> listByTitle(String title) {
		return bookDao.listByTitle(title);
	}

	public List<Book> listByAuthor(String userName) {
		Author author = authorDao.findByUsername(userName);
		if (author != null) {
			return author.getBooks();
		}
		return null;
	}

	public void setBookDao(BookDAO bookDao) {
		this.bookDao = bookDao;
	}

	public void setAuthorDao(AuthorDAO authorDao) {
		this.authorDao = authorDao;
	}

	public void setActivityDao(ActivityDAO activityDao) {
		this.activityDao = activityDao;
	}

}
