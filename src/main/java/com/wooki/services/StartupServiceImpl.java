package com.wooki.services;

import org.springframework.context.ApplicationContext;

import com.wooki.domain.model.Author;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

public class StartupServiceImpl implements StartupService {

	public StartupServiceImpl(ApplicationContext applicationContext) {

		BookManager bookManager = (BookManager) applicationContext
				.getBean("bookManager");
		ChapterManager chapterManager = (ChapterManager) applicationContext
				.getBean("chapterManager");
		AuthorManager authorManager = (AuthorManager) applicationContext
				.getBean("authorManager");

		// Add author to the book
		Author john = new Author();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("john");
		john.setPassword("password");
		authorManager.addAuthor(john);

		Author robink = new Author();
		robink.setEmail("robin@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		authorManager.addAuthor(robink);

		// Create books
		Book productBook = bookManager.create(
				"Tapestry 5 : When art meets Development", "john");
		Book cacheBook = bookManager.create("My Cache Product Book", "john");

		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook,
				"Requirements", "john");
		chapterManager.updateContent(chapterOne, "<p>You will need ...</p>");

		// Add robin to author's list
		bookManager.addAuthor(productBook, "robink");

		Chapter chapterTwo = bookManager.addChapter(productBook,
				"Installation", "robink");

		chapterManager.updateContent(chapterTwo,
				"<p>First you have to set environment variables...</p>");

	}

}
