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

import static org.apache.commons.io.FilenameUtils.separatorsToUnix;
import static org.apache.commons.lang.StringUtils.substring;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Context;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import com.carbonfive.db.jdbc.DatabaseType;
import com.carbonfive.db.jdbc.DatabaseUtils;
import com.carbonfive.db.jdbc.schema.CreateDatabase;
import com.carbonfive.db.jdbc.schema.DropDatabase;
import com.carbonfive.db.migration.DriverManagerMigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import com.carbonfive.db.migration.SimpleVersionStrategy;
import com.wooki.WookiSymbolsConstants;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;

public class StartupServiceImpl implements StartupService
{
    private Context context;

    private Logger logger;

    private String migrationsPath;

    private Session session;

    private BookManager bookManager;

    private ChapterManager chapterManager;

    private UserManager userManager;
    
    private String driver;
    
    private String url;
    
    private String username;
    
    private String password;
    

    public StartupServiceImpl(Logger logger, ApplicationContext applicationContext,
            @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
            @Inject @Symbol(WookiSymbolsConstants.MIGRATIONS_PATH) String migrationsPath,
            Context context, BookManager bookManager, ChapterManager chapterManager,
            UserManager userManager, Session session) throws UserAlreadyException,
            AuthorizationException
    {
        this.session = session;
        this.logger = logger;
        this.bookManager = bookManager;
        this.chapterManager = chapterManager;
        this.userManager = userManager;
        this.migrationsPath = migrationsPath;
        this.context = context;
        
        Configuration configuration = new Configuration();
        configuration.configure();

        this.driver = configuration.getProperty("hibernate.connection.driver_class");
        this.url = configuration.getProperty("hibernate.connection.url");
        this.username = configuration.getProperty("hibernate.connection.username");
        this.password = configuration.getProperty("hibernate.connection.password");


        initialize();
    }

    protected void initialize()
    {
        // enabled headless mode
        System.setProperty("java.awt.headless", "true");
        checkDatabaseCreation();

        /*
         * WookiSecurityContext securityCtx = (WookiSecurityContext) applicationContext
         * .getBean("wookiSecurityContext"); // Bypass creation if already done if
         * (userManager.findByUsername("ccordenier") != null) { return; } User ccordenier = new
         * User(); ccordenier.setEmail("christophe@gmail.com");
         * ccordenier.setUsername("ccordenier"); ccordenier.setPassword("password");
         * ccordenier.setFullname("Christophe C."); userManager.addUser(ccordenier); User gounthar =
         * new User(); gounthar.setEmail("bruno@gmail.com"); gounthar.setUsername("bverachten");
         * gounthar.setPassword("password"); gounthar.setFullname("Bruno V.");
         * userManager.addUser(gounthar); User robink = new User();
         * robink.setEmail("robin@gmail.com"); robink.setUsername("robink");
         * robink.setPassword("password"); robink.setFullname("Robin K.");
         * userManager.addUser(robink); securityCtx.log(robink); // Create books Book bookOfWooki =
         * bookManager.create("The book of Wooki"); // publish Abstract Chapter bookAbstract =
         * chapterManager.listChaptersInfo(bookOfWooki.getId()).get(0); chapterManager
         * .updateAndPublishContent( bookAbstract.getId(),
         * "<p>What would you need if you had to write something and share it with someone else? We think you would be looking for Wooki : a <strong>publish platform</strong> offering the possibility to have <strong>direct feedback</strong> on what you have written.</p>"
         * ); // Create new chapters and modify its content Chapter chapterOne =
         * bookManager.addChapter( bookOfWooki, "Collaborative document publishing"); chapterManager
         * .updateAndPublishContent( chapterOne.getId(),
         * "<p>The desire to make Wooki came from a finding: we noticed that the documentation we wrote every days at work did not provide the expected result.</p>"
         * +
         * "<p>We used to produce lots of documents on multiples formats: Word, PDF, Powerpoint and of course on our intranet knowledge base."
         * + "Each format used to have its inconvenient:</p>" + "<ul>" +
         * "<li>Intranet knowledge base wasn't enough corporate</li>" +
         * "<li>Word and PDF contents were not indexed by our intranet crawlers.</li>" + "</ul>" +
         * "<p>And the worst was that we almost never had feedback on these documents. We did not knew if the produced documentation was good enough."
         * +
         * " Why? <strong>Because we did not proposed an easy way to collaborate with people</strong>.</p>"
         * +
         * "<p>Wooki's goal is to suggest a solution to all theses problems. Of course, Wooki is not finished, that is just the beginning of the story.</p>"
         * ); // Add robin to author's list try { bookManager.addAuthor(bookOfWooki, "ccordenier");
         * bookManager.addAuthor(bookOfWooki, "bverachten"); } catch (Exception e) {
         * e.printStackTrace(); } Publication publication =
         * chapterManager.getLastPublishedPublication(chapterOne.getId());
         * chapterManager.addComment(publication.getId(), "This is a good starting point", "b20");
         * Chapter chapterTwo = bookManager.addChapter(bookOfWooki, "Open source contribution");
         * chapterManager .updateAndPublishContent( chapterTwo.getId(),
         * "<p>We are working since many months (...years?) on the web framework Tapestry 5. In some way, we always wanted to contribute to it as some kind of \"<quote>thank you</quote>\".</p>"
         * +
         * "<p>Doing a project like Wooki was perfect to show the possibilities of this framework: technologies integration, ease of development. </p>"
         * ); Chapter chapterTree = bookManager.addChapter(bookOfWooki, "Get started");
         * chapterManager .updateAndPublishContent( chapterTree.getId(), "<h3>Try the demo</h3>" +
         * "<p>If you are reading that, you must already know that we host a demo website. You can freely:</p>"
         * + "<ul>" + "<li>create accounts</li>" + "<li>create books</li>" +
         * "<li>export them as PDF</li>" + "</ul>" +
         * "<h3>Build Wooki directly from the source code</h3>" +
         * "<p>Considering that you have already manipulated Tapestry 5 and maven, you can build the application directly from the source code.</p>"
         * +
         * "<p>Go on <a href=\"http://github.com/robink/wooki\">Github</a>, download the sources and run jetty executing this command:</p>"
         * + "<pre>" + "mvn jetty:run" + "</pre>" + "<p>Launch your browser and that's it!</p>"); //
         * Do an update for testing purpose on book Abstract chapterManager .updateContent(
         * bookAbstract.getId(),
         * "<p>What would you need if you had to write something and share it with someone else? We think you would be looking for Wooki : a <strong>publish platform</strong> offering the possibility to have <strong>direct feedback</strong> on what you have written.</p>"
         * ); // Add a comment securityCtx.log(ccordenier); publication =
         * chapterManager.getLastPublishedPublication(bookAbstract.getId());
         * chapterManager.addComment(publication.getId(), "Wooki is really cool !", "b10");
         */
    }

    private void checkDatabaseCreation()
    {
        try
        {
            Query query = session.createSQLQuery("select count(*) from " + SimpleVersionStrategy.DEFAULT_VERSION_TABLE).setCacheable(
                    false);
            Integer val = (Integer) query.uniqueResult();
        }
        catch (HibernateException fail)
        {
            logger
                    .info("Database is not created or correctly initialized. Dropping any existing schema and creating a new one.");

     
            logger.info("Resetting database " + url + ".");

            try
            {
                logger.info("Dropping database " + url + ".");
                String dropSql = DropDatabase.DROP_DATABASE_SQL;
                new DropDatabase(driver, url, username, password).execute(dropSql);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException("Failed to reset database " + url, e);
            }
            catch (SQLException ignored)
            {
            }

            try
            {
                logger.info("Creating database " + url + ".");

                String createSql = CreateDatabase.CREATE_DATABASE_SQL;
                try
                {
                    new CreateDatabase(driver, url, username, password).execute(createSql);
                }
                // we try to continue to table manipulation to see if the database is really not created.
                catch (SQLException ignored)
                {
                }

                logger.info("Migrating database " + url + ".");
                createMigrationManager(driver, url, username, password).migrate();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Failed to reset database " + url, e);
            }
        }
    }
 
    private DriverManagerMigrationManager createMigrationManager(String driver, String url,
            String username, String password)
    {
        DriverManagerMigrationManager manager = new DriverManagerMigrationManager(driver, url,
                username, password, DatabaseType
                        .valueOf(DatabaseUtils.databaseType(url).toString()));

        String path = migrationsPath;

        if (path.startsWith("file:"))
        {
            path = substring(path, 5);
        }
        if (!path.startsWith("classpath:") && !path.startsWith("\"") && !path.startsWith("/"))
        {
            path = context.getRealFile(migrationsPath).getAbsolutePath();
        }
        path = separatorsToUnix(path);

        manager.setMigrationResolver(new ResourceMigrationResolver(path));

        SimpleVersionStrategy strategy = new SimpleVersionStrategy();
        strategy.setVersionTable(SimpleVersionStrategy.DEFAULT_VERSION_TABLE);
        strategy.setVersionColumn(SimpleVersionStrategy.DEFAULT_VERSION_COLUMN);
        strategy.setAppliedDateColumn(SimpleVersionStrategy.DEFAULT_APPLIED_DATE_COLUMN);
        strategy.setDurationColumn(SimpleVersionStrategy.DEFAULT_DURATION_COLUMN);
        manager.setVersionStrategy(strategy);

        return manager;
    }
}
