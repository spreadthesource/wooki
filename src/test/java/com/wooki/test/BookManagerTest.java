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

package com.wooki.test;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.wooki.domain.biz.ActivityManager;
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
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Test case for WookiManager service.
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class BookManagerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private BookManager bookManager;

	@Autowired
	private ChapterManager chapterManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private CommentManager commentManager;

	@Autowired
	private WookiSecurityContext securityCtx;

	@Autowired
	private DataSource ds;

	@Autowired
	private ActivityManager activityManager;

	@BeforeMethod
	public void initDb() throws UserAlreadyException, AuthorizationException {

		// Reset datas
		ClassPathResource script = new ClassPathResource("reset.sql");
		SimpleJdbcTemplate tpl = new SimpleJdbcTemplate(ds);
		SimpleJdbcTestUtils.executeSqlScript(tpl, script, true);

		// Add author to the book
		User john = new User();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("john");
		john.setFullname("John Doe");
		john.setPassword("password");
		userManager.addUser(john);

		securityCtx.log(john);

		// Create books
		Book productBook = bookManager.create("My First Product Book");
		Book cacheBook = bookManager.create("My Cache Product Book");

		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook, "Requirements");
		chapterManager.updateContent(chapterOne.getId(), "<p>You will need �� ...</p>");

		Chapter chapterTwo = bookManager.addChapter(productBook, "Installation");
		chapterManager.updateContent(chapterTwo.getId(), "<p>First you have to set environment variables...</p>");

	}

	@Test
	public void testActivity() throws UserAlreadyException, UserNotFoundException, UserAlreadyOwnerException {
		User robink = new User();
		robink.setEmail("robink@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink.setFullname("Robin Komiwes");
		userManager.addUser(robink);

		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		bookManager.addAuthor(myProduct, "robink");

		securityCtx.log(robink);

		Chapter chapter = bookManager.addChapter(myProduct, "Robin Book");
		chapterManager.updateAndPublishContent(chapter.getId(), "<p>Hello world from unit test cases...</p>");
		Publication published = chapterManager.getLastPublication(chapter.getId());
		Assert.assertNotNull(published);
		chapterManager.addComment(published.getId(), "Yes", "b1");

		Book robinsBook = bookManager.create("Robink book");

		// Verify users activites on john books
		User john = userManager.findByUsername("john");
		securityCtx.log(robink);

		List<Activity> activities = activityManager.listActivityOnUserBooks(10, john.getId());
		Assert.assertNotNull(activities);
		Assert.assertTrue(activities.size() > 0);
		for (Activity a : activities) {
			Assert.assertTrue(john.getId() != a.getUser().getId());
		}

		// Verify john activity on its book
		activities = activityManager.listActivityOnBook(10, john.getId());
		Assert.assertNotNull(activities);
		Assert.assertTrue(activities.size() > 0);
		for (Activity a : activities) {
			Assert.assertTrue(john.getId() == a.getUser().getId());
		}

		// Verify book creation activity
		activities = activityManager.listBookCreationActivity(10);
		Assert.assertNotNull(activities);
		Assert.assertEquals(activities.size(), 3);
		for (Activity a : activities) {
			Assert.assertTrue(a instanceof BookActivity);
		}

	}

	/**
	 * Verify that can we can load datas with manyToMany association.
	 * 
	 */
	@Test
	public void testListBookByUser() {
		List<Book> books = bookManager.listByUser("john");
		Assert.assertNotNull(books, "John has written books");
		Assert.assertEquals(books.size(), 2, "John has one book");
	}
	
	@Test
	public void testOwnerOfBook() {
		List<Book> books = bookManager.listByUser("john");
		Assert.assertNotNull(books, "John has written books");
		Assert.assertEquals(books.size(), 2, "John has two book");
		securityCtx.log(this.userManager.findByUsername("john"));
		securityCtx.isOwnerOfBook(books.get(0).getId());
	}

	/**
	 * Check that an author cannot add a chapter if he is not user of the book.
	 */
	@Test
	public void verifyCheckAuthor() throws UserAlreadyException, AuthorizationException {
		User robink = new User();
		robink.setEmail("robink@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink.setFullname("Robin Komiwes");
		userManager.addUser(robink);

		securityCtx.log(robink);

		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		try {
			bookManager.addChapter(myProduct, "My New Chapter");
			// Robin is not an author of the book
			Assert.fail("Robink should not be authorized to add a new chapter to 'my first product' book");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Verify like feature
	 */
	@Test
	public void testListBooksByTitle() {
		List<Book> books = bookManager.listByTitle("%Product%");
		Assert.assertNotNull(books, "John has written books");
		Assert.assertEquals(books.size(), 2, "John has one book");
	}

	/**
	 * Verify initial book list size.
	 * 
	 */
	@Test
	public void testListBook() {
		List<Book> books = bookManager.list();
		Assert.assertNotNull(books);
		Assert.assertEquals(books.size(), 2, "There is at two two books");
	}

	/**
	 * Verify that a title already in use generate an exception.
	 */
	@Test
	public void testChangeTitle() {

		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		try {
			myProduct.setTitle("My Cache Product Book");
			bookManager.updateTitle(myProduct);
			Assert.fail("Title should not be changed since it is already used.");
		} catch (TitleAlreadyInUseException taiuEx) {
			taiuEx.printStackTrace();
		}

		try {
			myProduct.setTitle("My new title");
			bookManager.updateTitle(myProduct);
		} catch (TitleAlreadyInUseException taiuEx) {
			Assert.fail("Title should be changed.");
			taiuEx.printStackTrace();
		}

	}

	@Test
	public void testChapterPreviousAndNext() {
		
		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct);
		Assert.assertNotNull(myProduct.getChapters());
		Assert.assertEquals(myProduct.getChapters().size(), 3);
		
		// Publish all the chapters
		for(Chapter chapter : myProduct.getChapters()) {
			chapterManager.updateAndPublishContent(chapter.getId(), "Some content...");
		}
		
		Object[] result = chapterManager.findPrevious(myProduct.getId(), myProduct.getChapters().get(1).getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(result.length, 2);
		Assert.assertEquals("Abstract", result[1]);
		result = chapterManager.findNext(myProduct.getId(), myProduct.getChapters().get(1).getId());
		Assert.assertNotNull(result);
		Assert.assertEquals("Installation", result[1]);
		Assert.assertEquals(result.length, 2);
		
		result = chapterManager.findPrevious(myProduct.getId(), myProduct.getChapters().get(0).getId());
		Assert.assertNull(result);

		result = chapterManager.findNext(myProduct.getId(), myProduct.getChapters().get(0).getId());
		Assert.assertEquals(result.length, 2);
		Assert.assertNotNull(result);
		Assert.assertEquals("Requirements", result[1]);
		
		result = chapterManager.findPrevious(myProduct.getId(), myProduct.getChapters().get(2).getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(result.length, 2);
		Assert.assertEquals("Requirements", result[1]);

		result = chapterManager.findNext(myProduct.getId(), myProduct.getChapters().get(2).getId());
		Assert.assertNull(result);

	}

	/**
	 * Verify initial chapter list size.
	 */
	@Test
	// TODO : this test needs to be updated as a chapter does not contains
	// content anymore
	public void testChapterValues() {
		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		// Verify chapter content
		// Chapter chapterOne = chapters.get(1);
		// Assert.assertEquals(new String(chapterOne.getContent()),
		// "<p>You will need �� ...</p>");
	}

	@Test
	// TODO : this test needs to be updated as a chapter does not contains
	// content anymore
	public void testUpdateChapterContent() {
		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		// Verify chapter content
		Chapter chapterOne = chapters.get(1);
		// Assert.assertTrue(new String(chapterOne.getContent())
		// .contains("<p>You will need �� ...</p>"));
	}

	/**
	 * Test sequence :
	 * <ul>
	 * <li>Get 'myProduct' book and verify initial list size</li>
	 * <li>Add a chapter and verify list size</li>
	 * <li>Remove Chapter and verify list size</li>
	 * </ul>
	 */
	@Test
	public void testChapterAdd() throws UserAlreadyException, AuthorizationException {

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
		chapterManager.updateContent(chapterThree.getId(), "<p>Sample Chapter Three Content</p>");

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
	 * 
	 */
	@Test
	public void testCommentAdd() {
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

		Comment com = chapterManager.addComment(published.getId(), "This paragraph doesn't make sense.", "c0");
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
	 */
	@Test
	public void testPublication() {

		Book myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct, "'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		String published = chapterManager.getLastPublishedContent(chapters.get(0).getId());
		Assert.assertNull(published, "No revision has been published.");

		// Update content and publish
		chapterManager.updateAndPublishContent(chapters.get(0).getId(), "<p>Tapestry is totally amazing</p>");
		chapterManager.publishChapter(chapters.get(0).getId());
		published = chapterManager.getLastPublishedContent(chapters.get(0).getId());
		Publication publication = chapterManager.getLastPublishedPublication(chapters.get(0).getId());
		Assert.assertNotNull(published, "No revision has been published.");
		Assert.assertEquals(published, "<p id=\"b" + publication.getId() + "0\" class=\"commentable\">Tapestry is totally amazing</p>");

	}
}
