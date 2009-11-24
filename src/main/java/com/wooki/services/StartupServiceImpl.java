package com.wooki.services;

import org.springframework.context.ApplicationContext;

import com.wooki.domain.model.User;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

public class StartupServiceImpl implements StartupService {

	public StartupServiceImpl(ApplicationContext applicationContext) {

		BookManager bookManager = (BookManager) applicationContext
				.getBean("bookManager");
		ChapterManager chapterManager = (ChapterManager) applicationContext
				.getBean("chapterManager");
		UserManager authorManager = (UserManager) applicationContext
				.getBean("authorManager");

		// Add author to the book
		User john = new User();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("john");
		john.setPassword("password");
		authorManager.addUser(john);

		User robink = new User();
		robink.setEmail("robin@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		authorManager.addUser(robink);

		// Create books
		Book productBook = bookManager.create(
				"Tapestry 5 : When art meets Development", "john");
		Book cacheBook = bookManager.create("My Cache Product Book", "john");

		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook,
				"Requirements", "john");
		chapterManager.updateContent(chapterOne.getId(), "<p>You will need ...</p>");

		// Add robin to author's list
		bookManager.addAuthor(productBook, "robink");

		Chapter chapterTwo = bookManager.addChapter(productBook,
				"Installation", "robink");

		chapterManager.updateContent(chapterTwo.getId(),
				"<p>First you have to set environment variables...</p>");

	}

}
