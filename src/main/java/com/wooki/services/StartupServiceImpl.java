//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.springframework.context.ApplicationContext;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

public class StartupServiceImpl implements StartupService {

	public StartupServiceImpl(ApplicationContext applicationContext, @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode)
			throws UserAlreadyException, AuthorizationException {
		// enabled headless mode
		System.setProperty("java.awt.headless", "true");

		if (!productionMode) {
			WookiSecurityContext securityCtx = (WookiSecurityContext) applicationContext.getBean("wookiSecurityContext");
			BookManager bookManager = (BookManager) applicationContext.getBean("bookManager");
			ChapterManager chapterManager = (ChapterManager) applicationContext.getBean("chapterManager");
			UserManager userManager = (UserManager) applicationContext.getBean("userManager");

			User ccordenier = new User();
			ccordenier.setEmail("christophe@gmail.com");
			ccordenier.setUsername("ccordenier");
			ccordenier.setPassword("password");
			ccordenier.setFullname("Christophe C.");
			userManager.addUser(ccordenier);

			User gounthar = new User();
			gounthar.setEmail("bruno@gmail.com");
			gounthar.setUsername("bverachten");
			gounthar.setPassword("password");
			gounthar.setFullname("Bruno V.");
			userManager.addUser(gounthar);

			User robink = new User();
			robink.setEmail("robin@gmail.com");
			robink.setUsername("robink");
			robink.setPassword("password");
			robink.setFullname("Robin K.");
			userManager.addUser(robink);
			
			securityCtx.log(robink);			
			
			// Create books
			Book bookOfWooki = bookManager.create("The book of Wooki");

			// publish Abstract
			Chapter bookAbstract = chapterManager.listChaptersInfo(bookOfWooki.getId()).get(0);
			chapterManager
					.updateAndPublishContent(
							bookAbstract.getId(),
							"<p>What would you need if you had to write something and share it with people? We think you would be looking for Wooki : a publish platform offering the possibility to have direct feedback on what you have written.</p>");
			
			// Create new chapters and modify its content
			Chapter chapterOne = bookManager.addChapter(bookOfWooki, "Collaborative document publishing");
			chapterManager
					.updateAndPublishContent(
							chapterOne.getId(),
							"<p>The will to make Wooki came from a constatation. We noticed that the documentation we write every day at work did not get the expected result.</p>"
									+ "<p>We used to produce lots of documents on multiples formats: Word, PDF, Powerpoint and of course on our intranet knowledge base.</p>"
									+ "<p>Each format used to have its inconvenient: intranet knowledge base wasn't enough corporate, Word and PDF contents weren't indexed by our internal crawlers.</p>"
									+ "<p>And the worst was that we almost never had  feedback on them. Why? Because we didn't proposed an easy way to collaborate with people</p>"
									+ "<p>Wooki's goal is to offer a solution to all theses problems. Of course, Wooki is not finished, but we hope it will encounter a big future.</p>");

			// Add robin to author's list
			try {
				bookManager.addAuthor(bookOfWooki, "ccordenier");
				bookManager.addAuthor(bookOfWooki, "bverachten");
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
			Chapter chapterTwo = bookManager.addChapter(bookOfWooki, "Open source contribution");

			chapterManager.updateAndPublishContent(chapterTwo.getId(), "<p>We are working since many months (...years?) on the web framework Tapestry 5. We always wanted to contribute in some way to it.</p>" +
					"<p>Doing a project like Wooki was perfect to show the possibilities of this framework: technologies integration, ease of development. </p>");



			Chapter chapterTree = bookManager.addChapter(bookOfWooki, "Get started");

			chapterManager.updateAndPublishContent(chapterTree.getId(), "<h3>Try the demo</h3>" +
					"<p>If you are reading that, you must already know that we host a demo website. You can freely create accounts and try Wooki there. </p>" +
					"<h3>Build Wooki directly from the source code</h3>" +
					"<p>Considering that you have already manipulated Tapestry 5 and maven, you can build the application directly from the source code.</p>" +
					"<p>Go on Github, download the sources and run jetty executing this command:</p>" +
					"<pre>" +
					"mvn jetty:run" +
					"</pre>" +
					"<p>Launch your browser and that's it!</p>");

		}
	}
}
