package com.wooki.services;

import org.springframework.context.ApplicationContext;

import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

public class StartupServiceImpl implements StartupService {

	public StartupServiceImpl(ApplicationContext applicationContext) throws UserAlreadyException, AuthorizationException {

		BookManager bookManager = (BookManager) applicationContext
				.getBean("bookManager");
		ChapterManager chapterManager = (ChapterManager) applicationContext
				.getBean("chapterManager");
		UserManager userManager = (UserManager) applicationContext
				.getBean("userManager");

		// Add author to the book
		User john = new User();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("john");
		john.setPassword("password");
		john.setFullname("John Doe");
		userManager.addUser(john);
		

		User robink = new User();
		robink.setEmail("robin@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink.setFullname("Robin K.");
		userManager.addUser(robink);

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
