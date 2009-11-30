package com.wooki.test;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;

/**
 * Test case for WookiManager service.
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class BookManagerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private BookManager bookManager;

	@Autowired
	private ChapterManager chapterManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private CommentManager commentManager;

	@Autowired
	private DataSource ds;
	
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

		// Create books
		Book productBook = bookManager.create("My First Product Book", john
				.getUsername());
		Book cacheBook = bookManager.create("My Cache Product Book", john
				.getUsername());

		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook,
				"Requirements", john.getUsername());
		chapterManager.updateContent(chapterOne.getId(),
				"<p>You will need Žˆ ...</p>");

		Chapter chapterTwo = bookManager.addChapter(productBook,
				"Installation", john.getUsername());
		chapterManager.updateContent(chapterTwo.getId(),
				"<p>First you have to set environment variables...</p>");

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

	/**
	 * Check that an author cannot add a chapter if he is not user of the book.
	 */
	@Test
	public void verifyCheckAuthor() throws UserAlreadyException,
			AuthorizationException {
		User robink = new User();
		robink.setEmail("robink@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink.setFullname("Robin Komiwes");
		userManager.addUser(robink);

		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		try {
			bookManager.addChapter(myProduct, "My New Chapter", "robink");
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
	 * Verify initial chapter list size.
	 */
	@Test
	public void testChapterValues() {
		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		// Verify chapter content
		Chapter chapterOne = chapters.get(1);
		Assert
				.assertEquals(new String(chapterOne.getContent()),
						"<p>You will need Žˆ ...</p>");
	}

	@Test
	public void testUpdateChapterContent() {
		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		// Verify chapter content
		Chapter chapterOne = chapters.get(1);
		Assert.assertTrue(new String(chapterOne.getContent()).contains(
				"<p>You will need Žˆ ...</p>"));
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
	public void testChapterAdd() throws UserAlreadyException,
			AuthorizationException {

		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		// Add a chapter and do search again
		Chapter chapterThree = bookManager.addChapter(myProduct,
				"Chapter Tree", "john");
		chapterManager.updateContent(chapterThree.getId(),
				"<p>Sample Chapter Three Content</p>");

		myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");
		chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 4, "Chapter count is incorrect.");

		// Remove the added chapter and verify the list again
		chapterManager.delete(chapterThree);
		myProduct = bookManager.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");
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
		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		User john = userManager.findByUsername("john");
		Long chapterId = chapters.get(1).getId();
		
		chapterManager.publishChapter(chapterId);
		Publication published = chapterManager.getLastPublished(chapterId);
		Assert.assertNotNull(published);
		
		Comment com = chapterManager.addComment(published.getId(), john,
				"This paragraph doesn't make sense.", "c0");
		List<Comment> openComs = commentManager.listOpenForPublication(published.getId());

		Assert.assertNotNull(openComs);
		Assert.assertEquals(openComs.size(), 1,
				"There is at least one open comment.");
		
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

		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		List<Chapter> chapters = chapterManager.listChapters(myProduct.getId());
		Assert.assertNotNull(chapters, "Chapters are missing.");
		Assert.assertEquals(chapters.size(), 3, "Chapter count is incorrect.");

		String published = chapterManager.getLastPublishedContent(chapters
				.get(0).getId());
		Assert.assertNull(published, "No revision has been published.");

		// Update content and publish
		chapterManager.updateContent(chapters.get(0).getId(),
				"<p>Tapestry is totally amazing</p>");
		chapterManager.publishChapter(chapters.get(0).getId());
		published = chapterManager.getLastPublishedContent(chapters.get(0)
				.getId());
		Assert.assertNotNull(published, "No revision has been published.");
		Assert.assertEquals(published,
				"<p id=\"b0\" class=\"commentable\">Tapestry is totally amazing</p>");

	}
}
