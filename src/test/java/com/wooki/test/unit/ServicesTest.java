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

package com.wooki.test.unit;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.Draft;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.SearchEngine;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Test case for WookiManager service.
 */
public class ServicesTest extends AbstractWookiUnitTestSuite
{

    private BookManager bookManager;

    private SearchEngine searchEngine;

    private ChapterManager chapterManager;

    private UserManager userManager;

    private CommentManager commentManager;

    private WookiSecurityContext securityCtx;

    private DataSource ds;

    // private ActivitySource activityManager;

    @BeforeClass
    public void setupServices() throws UserAlreadyException, AuthorizationException
    {
        bookManager = registry.getService(BookManager.class);
        chapterManager = registry.getService(ChapterManager.class);
        userManager = registry.getService(UserManager.class);
        commentManager = registry.getService(CommentManager.class);
        // activityManager = registry.getService(ActivitySource.class);

        searchEngine = context.getBean(SearchEngine.class);
        securityCtx = context.getBean(WookiSecurityContext.class);
        ds = context.getBean(DriverManagerDataSource.class);

        // Add author to the book
        User john = new User();
        john.setEmail("john.doe@gmail.com");
        john.setUsername("john");
        john.setFullname("John Doe");
        john.setPassword("password");
        userManager.registerUser(john);

        securityCtx.log(john);

        // Create books
        Book productBook = bookManager.create("My First Product Book");
        Book cacheBook = bookManager.create("My Cache Product Book");
        Book wildcardTitle = bookManager.create("!%_");

        // Create new chapters and modify its content
        Chapter chapterOne = bookManager.addChapter(productBook, "Requirements");
        Draft draft = new Draft();
        draft.setData("<p>You will need éé ...</p>");
        draft.setTimestamp(chapterOne.getLastModified());
        chapterManager.updateContent(chapterOne.getId(), draft);

        Chapter chapterTwo = bookManager.addChapter(productBook, "Installation");
        draft.setData("<p>First you have to set environment variables...</p>");
        draft.setTimestamp(chapterTwo.getLastModified());
        chapterManager.updateContent(chapterTwo.getId(), draft);
    }

    @AfterClass
    public void resetDb()
    {
        // Reset datas
        // ClassPathResource script = new ClassPathResource("reset.sql");
        // SimpleJdbcTemplate tpl = new SimpleJdbcTemplate(ds);
        // SimpleJdbcTestUtils.executeSqlScript(tpl, script, true);
    }

    @Test(enabled = true)
    public void testActivity() throws UserAlreadyException, UserNotFoundException,
            UserAlreadyOwnerException
    {
        User robink = getOrCreate("robink");

        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        User john = userManager.findByUsername("john");
        Assert.assertNotNull(john, "John exists...");
        securityCtx.log(john);

        bookManager.addAuthor(myProduct, "robink");

        securityCtx.log(robink);

        Chapter chapter = bookManager.addChapter(myProduct, "Robin Book");
        Draft draft = new Draft();
        draft.setTimestamp(chapter.getLastModified());
        draft.setData("<p>Hello world from unit test cases...</p>");
        chapterManager.updateAndPublishContent(chapter.getId(), draft);
        Publication published = chapterManager.getRevision(chapter.getId(), null);
        Assert.assertNotNull(published);
        chapterManager.addComment(published.getId(), "Yes", "b1");

        Book robinsBook = bookManager.create("Robink book");

        // Verify users activites on john books
        securityCtx.log(robink);

        // TODO Uncomment and develop
        /**
         * List<Activity> activities = activityManager.listActivityOnUserBooks(0, 10, john.getId());
         * Assert.assertNotNull(activities); Assert.assertTrue(activities.size() > 0); for (Activity
         * a : activities) { Assert.assertTrue(john.getId() != a.getUser().getId()); } // Verify
         * john activity on its book activities = activityManager.listActivityOnBook(0, 10,
         * john.getId()); Assert.assertNotNull(activities); Assert.assertTrue(activities.size() >
         * 0); for (Activity a : activities) { Assert.assertTrue(john.getId() ==
         * a.getUser().getId()); } // Verify book creation activity activities =
         * activityManager.listBookCreationActivity(0, 10); Assert.assertNotNull(activities);
         * Assert.assertEquals(activities.size(), 4); for (Activity a : activities) {
         * Assert.assertTrue(a instanceof BookActivity); }
         */

        // Remove last added elements
        chapterManager.remove(chapter.getId());
        bookManager.remove(robinsBook.getId());

    }

    /**
     * Verify that can we can load datas with manyToMany association.
     */
    @Test(enabled = true)
    public void testListBookByUser()
    {
        List<Book> books = bookManager.listByOwner("john");
        Assert.assertNotNull(books, "John has written books");
        Assert.assertEquals(books.size(), 3, "John has one book");
    }

    @Test(enabled = true)
    public void testOwnerOfBook()
    {
        List<Book> books = bookManager.listByOwner("john");
        Assert.assertNotNull(books, "John has written books");
        Assert.assertEquals(books.size(), 3, "John has three book");
        securityCtx.log(this.userManager.findByUsername("john"));
        securityCtx.isOwner(books.get(0));
    }

    /**
     * Check that an author cannot add a chapter if he is not user of the book.
     */
    @Test(enabled = true)
    public void verifyCheckAuthor() throws UserAlreadyException, AuthorizationException
    {
        User jean = getOrCreate("jean");

        securityCtx.log(jean);

        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        try
        {
            bookManager.addChapter(myProduct, "My New Chapter");
            // Jean is not an author of the book
            Assert
                    .fail("Jean should not be authorized to add a new chapter to 'my first product' book");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Verify like feature
     */
    @Test(enabled = true)
    public void testListBooksByTitle()
    {
        List<Book> books = bookManager.listByTitle("%Product%");
        Assert.assertNotNull(books, "listBytTitle should never return null value but empty list");
        Assert.assertEquals(books.size(), 2, "John has one book");

        books = this.searchEngine.findBook("Product");
        Assert.assertNotNull(books, "findBook should never return null value but empty list");
        Assert.assertEquals(books.size(), 2, "John has one book");

        books = this.searchEngine.findBook("%");
        Assert.assertEquals(books.size(), 1, "Wildcard must be escaped from search query");

        books = this.searchEngine.findBook("_");
        Assert.assertEquals(books.size(), 1, "Wildcard must be escaped from search query");

        books = this.searchEngine.findBook("!");
        Assert.assertEquals(books.size(), 1, "Wildcard must be escaped from search query");

    }

    /**
     * Verify initial book list size.
     */
    @Test(enabled = true)
    public void testListBook()
    {
        List<Book> books = bookManager.list();
        Assert.assertNotNull(books);
        Assert.assertEquals(books.size(), 3, "There is at least three books");
    }

    /**
     * Verify that a title already in use generate an exception.
     * 
     * @throws UserAlreadyException
     */
    @Test(enabled = true)
    public void testChangeTitle() throws UserAlreadyException
    {

        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        User john = getOrCreate("john");
        securityCtx.log(john);

        try
        {
            myProduct.setTitle("My Cache Product Book");
            bookManager.updateTitle(myProduct);
            Assert.fail("Title should not be changed since it is already used.");
        }
        catch (TitleAlreadyInUseException taiuEx)
        {
            taiuEx.printStackTrace();
        }

        try
        {
            myProduct.setTitle("My new title");
            bookManager.updateTitle(myProduct);

            // Get back to the previous title
            myProduct.setTitle("My first product book");
            bookManager.updateTitle(myProduct);
        }
        catch (TitleAlreadyInUseException taiuEx)
        {
            Assert.fail("Title should be changed.");
            taiuEx.printStackTrace();
        }

    }

    @Test(enabled = true)
    public void testChapterPreviousAndNext() throws UserAlreadyException
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct);
        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters);
        Assert.assertEquals(chapters.size(), 3);

        User john = getOrCreate("john");
        this.securityCtx.log(john);

        // Publish all the chapters
        Draft draft = new Draft();
        for (Chapter chapter : chapterManager.listChapters(myProduct.getId()))
        {
            draft.setTimestamp(chapter.getLastModified());
            draft.setData("Some content...");
            chapterManager.updateAndPublishContent(chapter.getId(), draft);
        }

        Object[] result = chapterManager.findPrevious(myProduct.getId(), chapters.get(1).getId());
        Assert.assertNull(result);
        result = chapterManager.findNext(myProduct.getId(), chapters.get(1).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("Installation", result[1]);
        Assert.assertEquals(result.length, 2);

        result = chapterManager.findPrevious(myProduct.getId(), chapters.get(0).getId());
        Assert.assertNull(result);

        result = chapterManager.findNext(myProduct.getId(), chapters.get(0).getId());
        Assert.assertEquals(result.length, 2);
        Assert.assertNotNull(result);
        Assert.assertEquals("Requirements", result[1]);

        result = chapterManager.findPrevious(myProduct.getId(), chapters.get(2).getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.length, 2);
        Assert.assertEquals("Requirements", result[1]);

        result = chapterManager.findNext(myProduct.getId(), chapters.get(2).getId());
        Assert.assertNull(result);
    }

    /**
     * Verify initial chapter list size.
     */
    @Test(enabled = true)
    // TODO : this test needs to be updated as a chapter does not contains
    // content anymore
    public void testChapterValues()
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        // Verify chapter content
        // Chapter chapterOne = chapters.get(1);
        // Assert.assertEquals(new String(chapterOne.getContent()),
        // "<p>You will need éé ...</p>");
    }

    @Test(enabled = true)
    // TODO : this test needs to be updated as a chapter does not contains
    // content anymore
    public void testUpdateChapterContent()
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        // Verify chapter content
        Chapter chapterOne = chapters.get(1);
        // Assert.assertTrue(new String(chapterOne.getContent())
        // .contains("<p>You will need éé ...</p>"));
    }

    /**
     * This test tries to reorder the list of chapters for a given book.
     */
    @Test(enabled = true)
    public void testUpdateChapterIndex()
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        Chapter toMove = chapters.get(0);
        bookManager.updateChapterIndex(myProduct.getId(), toMove.getId(), 1);

        chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");
        Assert.assertTrue(chapters.get(1).getId().equals(toMove.getId()));

        // Reset to initial order
        bookManager.updateChapterIndex(myProduct.getId(), toMove.getId(), 0);
    }

    /**
     * Test sequence :
     * <ul>
     * <li>Get 'myProduct' book and verify initial list size</li>
     * <li>Add a chapter and verify list size</li>
     * <li>Remove Chapter and verify list size</li>
     * </ul>
     */
    @Test(enabled = true)
    public void testChapterAdd() throws UserAlreadyException, AuthorizationException
    {

        User john = userManager.findByUsername("john");
        Assert.assertNotNull(john, "John exists...");
        securityCtx.log(john);

        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        // Add a chapter and do search again
        Chapter chapterThree = bookManager.addChapter(myProduct, "Chapter Tree");
        Draft draft = new Draft();
        draft.setTimestamp(chapterThree.getLastModified());
        draft.setData("<p>Sample Chapter Three Content</p>");
        chapterManager.updateContent(chapterThree.getId(), draft);

        myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");
        chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 4, "Chapter count is incorrect.");

        // Remove the added chapter and verify the list again
        chapterManager.remove(chapterThree.getId());
        myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");
        chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");
    }

    /**
     * Verify if we can simply add comment on chapters.
     */
    @Test(enabled = true)
    public void testCommentAdd()
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        User john = userManager.findByUsername("john");
        Long chapterId = chapters.get(1).getId();

        chapterManager.publishChapter(chapterId);
        Publication published = chapterManager.getLastPublishedPublication(chapterId);
        Assert.assertNotNull(published);

        securityCtx.log(john);

        Comment com = chapterManager.addComment(
                published.getId(),
                "This paragraph doesn't make sense.",
                "c0");
        List<Comment> openComs = commentManager.listOpenForPublication(published.getId());

        Assert.assertNotNull(openComs);
        Assert.assertEquals(openComs.size(), 1, "There is at least one open comment.");

        List<Object[]> comInfos = commentManager.listCommentInfos(published.getId());
        Assert.assertNotNull(comInfos);
        Assert.assertEquals(1, comInfos.size());
        Assert.assertEquals("c0", comInfos.get(0)[0]);
        Assert.assertEquals(new Long(1), comInfos.get(0)[1]);
    }

    /**
     * Verify if publication mechanism creates the good entities and content.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(enabled = true)
    public void testPublicationIso()
    {
        System.setProperty("file.encoding", "ISO-8859-1");
        this.publication();
    }

    /**
     * Verify if publication mechanism creates the good entities and content.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(enabled = true)
    public void testPublicationUtf()
    {
        System.setProperty("file.encoding", "UTF-8");
        this.publication();
    }

    /**
     * Verify if publication mechanism creates the good entities and content.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test(enabled = true)
    public void testPublicationCp()
    {
        System.setProperty("file.encoding", "cp1252");
        this.publication();
    }

    public void publication()
    {
        Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
        Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

        List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
        Assert.assertNotNull(chapters, "Chapters are missing.");
        Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

        // String published = chapterManager.getLastPublishedContent(chapters.get(0).getId());
        // Assert.assertNull(published, "No revision has been published.");

        // Update content and publish
        Draft draft = new Draft();
        draft.setData("<p>Tapestry is totally amazing éàê</p>");
        draft.setTimestamp(chapters.get(0).getLastModified());
        chapterManager.updateAndPublishContent(chapters.get(0).getId(), draft);
        chapterManager.publishChapter(chapters.get(0).getId());
        String published = chapterManager.getLastPublishedContent(chapters.get(0).getId());
        Publication publication = chapterManager.getLastPublishedPublication(chapters.get(0)
                .getId());
        Assert.assertNotNull(published, "No revision has been published.");
        Assert.assertEquals(published, "<p id=\"b" + publication.getId()
                + "0\" class=\"commentable\">Tapestry is totally amazing éàê</p>");
    }

    /**
     * Verify that we cannot create a user that already exists.
     */
    @Test(enabled = true)
    public void checkUserExists()
    {
        User user = userManager.findByUsername("JoHN");
        Assert.assertNotNull(user, "John doe exist.");
    }

    /**
     * Verifies that an exception is thrown when adding an author that already exists.
     */
    @Test(enabled = true)
    public void verifyAuthorAlreadyExists()
    {
        User john = new User();
        john.setEmail("john.doe@hotmail.com");
        john.setUsername("john");
        john.setFullname("John Doe");
        john.setPassword("passpass");
        try
        {
            userManager.registerUser(john);
            Assert.fail("User john already exists, call add must raise an exception.");
        }
        catch (UserAlreadyException uaEx)
        {
            uaEx.printStackTrace();
        }
    }

    /**
     * Verify that findByUsername is case insensitive.
     */
    @Test(enabled = true)
    public void checkFindByUserName()
    {
        User user = userManager.findByUsername("JOHn");
        Assert.assertNotNull(user);
    }

    /**
     * Simple create a user with the given id if it does not already exists.
     * 
     * @param username
     * @return
     * @throws UserAlreadyException
     */
    private User getOrCreate(String username) throws UserAlreadyException
    {
        User user = this.userManager.findByUsername(username);

        if (user != null) { return user; }

        user = new User();
        user.setEmail(username + "@gmail.com");
        user.setUsername(username);
        user.setPassword("password");
        user.setFullname(username);
        userManager.registerUser(user);
        return user;
    }

}
