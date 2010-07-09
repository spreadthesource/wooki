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

import javax.sql.DataSource;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.springframework.context.ApplicationContext;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

public class StartupServiceImpl implements StartupService
{

    public StartupServiceImpl(ApplicationContext applicationContext,
            @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
            BookManager bookManager, ChapterManager chapterManager, UserManager userManager,
            UserDAO userDao, DataSource datasource) throws UserAlreadyException,
            AuthorizationException
    {
        // enabled headless mode
        System.setProperty("java.awt.headless", "true");

        WookiSecurityContext securityCtx = (WookiSecurityContext) applicationContext
                .getBean("wookiSecurityContext");

        // Bypass creation if already done
        if (userManager.findByUsername("ccordenier") != null) { return; }
        User ccordenier = new User();
        ccordenier.setEmail("christophe@gmail.com");
        ccordenier.setUsername("ccordenier");
        ccordenier.setPassword("password");
        ccordenier.setFullname("Christophe C.");
        userManager.registerUser(ccordenier);
        User gounthar = new User();
        gounthar.setEmail("bruno@gmail.com");
        gounthar.setUsername("bverachten");
        gounthar.setPassword("password");
        gounthar.setFullname("Bruno V.");
        userManager.registerUser(gounthar);
        User robink = new User();
        robink.setEmail("robin@gmail.com");
        robink.setUsername("robink");

        robink.setPassword("password");
        robink.setFullname("Robin K.");
        userManager.registerUser(robink);
        securityCtx.log(robink);
        // Create books
        Book bookOfWooki = bookManager.create("The book of Wooki");
        // publish Abstract
        Chapter bookAbstract = chapterManager.listChaptersInfo(bookOfWooki.getId()).get(0);
        chapterManager
                .updateAndPublishContent(
                        bookAbstract.getId(),
                        "<p>What would you need if you had to write something and share it with someone else? We think you would be looking for Wooki : a <strong>publish platform</strong> offering the possibility to have <strong>direct feedback</strong> on what you have written.</p>"); // Create
        // new chapters and modify its content
        Chapter chapterOne = bookManager.addChapter(
                bookOfWooki,
                "Collaborative document publishing");
        chapterManager
                .updateAndPublishContent(
                        chapterOne.getId(),
                        "<p>The desire to make Wooki came from a finding: we noticed that the documentation we wrote every days at work did not provide the expected result.</p>"
                                + "<p>We used to produce lots of documents on multiples formats: Word, PDF, Powerpoint and of course on our intranet knowledge base."
                                + "Each format used to have its inconvenient:</p>"
                                + "<ul>"
                                + "<li>Intranet knowledge base wasn't enough corporate</li>"
                                + "<li>Word and PDF contents were not indexed by our intranet crawlers.</li>"
                                + "</ul>"
                                + "<p>And the worst was that we almost never had feedback on these documents. We did not knew if the produced documentation was good enough."
                                + " Why? <strong>Because we did not proposed an easy way to collaborate with people</strong>.</p>"
                                + "<p>Wooki's goal is to suggest a solution to all theses problems. Of course, Wooki is not finished, that is just the beginning of the story.</p>"); // Add
        // robin to author's list
        try
        {
            bookManager.addAuthor(bookOfWooki, "ccordenier");
            bookManager.addAuthor(bookOfWooki, "bverachten");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Publication publication = chapterManager.getLastPublishedPublication(chapterOne.getId());
        chapterManager.addComment(publication.getId(), "This is a good starting point", "b20");
        Chapter chapterTwo = bookManager.addChapter(bookOfWooki, "Open source contribution");
        chapterManager
                .updateAndPublishContent(
                        chapterTwo.getId(),
                        "<p>We are working since many months (...years?) on the web framework Tapestry 5. In some way, we always wanted to contribute to it as some kind of \"<quote>thank you</quote>\".</p>"
                                + "<p>Doing a project like Wooki was perfect to show the possibilities of this framework: technologies integration, ease of development. </p>");
        Chapter chapterTree = bookManager.addChapter(bookOfWooki, "Get started");
        chapterManager
                .updateAndPublishContent(
                        chapterTree.getId(),
                        "<h3>Try the demo</h3>"
                                + "<p>If you are reading that, you must already know that we host a demo website. You can freely:</p>"
                                + "<ul>"
                                + "<li>create accounts</li>"
                                + "<li>create books</li>"
                                + "<li>export them as PDF</li>"
                                + "</ul>"
                                + "<h3>Build Wooki directly from the source code</h3>"
                                + "<p>Considering that you have already manipulated Tapestry 5 and maven, you can build the application directly from the source code.</p>"
                                + "<p>Go on <a href=\"http://github.com/robink/wooki\">Github</a>, download the sources and run jetty executing this command:</p>"
                                + "<pre>" + "mvn jetty:run" + "</pre>"
                                + "<p>Launch your browser and that's it!</p>");
        // Do an update for testing purpose on book Abstract
        chapterManager
                .updateContent(
                        bookAbstract.getId(),
                        "<p>What would you need if you had to write something and share it with someone else? We think you would be looking for Wooki : a <strong>publish platform</strong> offering the possibility to have <strong>direct feedback</strong> on what you have written.</p>"); // Add
        // a comment
        securityCtx.log(ccordenier);
        publication = chapterManager.getLastPublishedPublication(bookAbstract.getId());
        chapterManager.addComment(publication.getId(), "Wooki is really cool !", "b10");

    }
}
