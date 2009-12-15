package com.wooki.services;

import org.springframework.context.ApplicationContext;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

public class StartupServiceImpl implements StartupService {

	public StartupServiceImpl(ApplicationContext applicationContext)
			throws UserAlreadyException, AuthorizationException {
		WookiSecurityContext securityCtx = (WookiSecurityContext) applicationContext.getBean("wookiSecurityContext");
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
		
		User ccordenier = new User();
		ccordenier.setEmail("christophe@gmail.com");
		ccordenier.setUsername("ccordenier");
		ccordenier.setPassword("password");
		ccordenier.setFullname("Christophe C.");
		userManager.addUser(ccordenier);

		User gounthar = new User();
		gounthar.setEmail("gounthar@gmail.com");
		gounthar.setUsername("gounthar");
		gounthar.setPassword("password");
		gounthar.setFullname("Bruno V.");
		userManager.addUser(gounthar);
		
		User robink = new User();
		robink.setEmail("robin@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink.setFullname("Robin K.");
		userManager.addUser(robink);

		securityCtx.log(john);
		
		// Create books
		Book productBook = bookManager.create(
				"Tapestry 5 : When development meets Art");
		Book cacheBook = bookManager.create("My Cache Product Book");
		
		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook,
				"Requirements");
		chapterManager.updateContent(chapterOne.getId(),
				"<p>You will need ...</p>");

		// Add robin to author's list
		try {
			bookManager.addAuthor(productBook, "robink");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		securityCtx.log(robink);
		Chapter chapterTwo = bookManager.addChapter(productBook,
				"Installation");

		chapterManager.updateContent(chapterTwo.getId(),
				"<p>First you have to set environment variables...</p>");

		// publish Abstract
		Chapter bookAbstract = chapterManager.listChaptersInfo(
				productBook.getId()).get(0);
		chapterManager
				.updateContent(
						bookAbstract.getId(),
						"<p>Apache Tapestry is an open-source framework for creating dynamic, robust, highly scalable web applications in Java. Tapestry complements and builds upon the standard Java Servlet API, and so it works in any servlet container or application server.</p><p>Tapestry divides a web application into a set of pages, each constructed from components. This provides a consistent structure, allowing the Tapestry framework to assume responsibility for key concerns such as URL construction and dispatch, persistent state storage on the client or on the server, user input validation, localization/internationalization, and exception reporting. Developing Tapestry applications involves creating HTML templates using plain HTML, and combining the templates with small amounts of Java code. In Tapestry, you create your application in terms of objects, and the methods and properties of those objects -- and specifically not in terms of URLs and query parameters. Tapestry brings true object oriented development to Java web applications.</p>");
		chapterManager.publishChapter(bookAbstract.getId());

		securityCtx.log(john);
		Publication published = chapterManager.getLastPublished(bookAbstract
				.getId());
		chapterManager.addComment(published.getId(), "Yes it's true !!! Yes it's true !!! Yes it's true !!!Yes it's true !!!Yes it's true !!!Yes it's true !!!Yes it's true !!!Yes it's true !!!Yes it's true !!! Yes it's true !!!Yes it's true !!! Yes it's true !!! Yes it's true !!!Yes it's true !!!", "b10");
		chapterManager
				.addComment(published.getId(), "I agree but ... :) I agree but ... :) I agree but ... :) I agree but ... :) v I agree but ... :) I agree but ... :) I agree but ... :) I agree but ... :)I agree but ... :) I agree but ... :) I agree but ... :) I agree but ... :) I agree but ... :)", "b10");

	}

}
