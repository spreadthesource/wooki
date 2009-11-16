package com.wooki.test;

import java.util.List;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.domain.model.Author;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.services.AuthorManager;
import com.wooki.services.BookManager;
import com.wooki.services.ChapterManager;
import com.wooki.services.CommentManager;

/**
 * Test case for WookiManager service.
 * 
 * @author ccordenier
 * 
 */
@ContextConfiguration(locations = { "/daoContext.xml",
		"/applicationContext.xml" })
public class BookManagerTest extends AbstractTestNGSpringContextTests {

	private BookManager bookManager;

	private ChapterManager chapterManager;

	private AuthorManager authorManager;

	private CommentManager commentManager;

	@BeforeClass
	public void initDb() {

		bookManager = (BookManager) applicationContext.getBean("bookManager");
		chapterManager = (ChapterManager) applicationContext
				.getBean("chapterManager");
		authorManager = (AuthorManager) applicationContext
				.getBean("authorManager");
		commentManager = (CommentManager) applicationContext
				.getBean("commentManager");

		// Add author to the book
		Author john = new Author();
		john.setEmail("john.doe@gmail.com");
		john.setUsername("john");
		john.setPassword("password");
		john = authorManager.addAuthor(john);

		// Create books
		Book productBook = bookManager.create("My First Product Book", john);
		Book cacheBook = bookManager.create("My Cache Product Book", john);

		// Create new chapters and modify its content
		Chapter chapterOne = bookManager.addChapter(productBook,
				"Requirements", john.getUsername());
		chapterManager.updateContent(chapterOne, "<p>You will need ...</p>");

		Chapter chapterTwo = bookManager.addChapter(productBook,
				"Installation", john.getUsername());
		chapterManager.updateContent(chapterTwo,
				"<p>First you have to set environment variables...</p>");

	}

	/**
	 * Verify that can we can load datas with manyToMany association.
	 * 
	 */
	@Test
	public void testListBookByAuthor() {
		List<Book> books = bookManager.listByAuthor("john");
		Assert.assertNotNull(books, "John has written books");
		Assert.assertEquals(books.size(), 2, "John has one book");
	}

	/**
	 * Check that an author cannot add a chapter if he is not author of the
	 * book.
	 */
	@Test
	public void verifyCheckAuthor() {
		Author robink = new Author();
		robink.setEmail("robink@gmail.com");
		robink.setUsername("robink");
		robink.setPassword("password");
		robink = authorManager.addAuthor(robink);

		Book myProduct = bookManager
				.findBookBySlugTitle("my-first-product-book");
		Assert.assertNotNull(myProduct,
				"'my-first-product-book' is not available.");

		try {
			bookManager.addChapter(myProduct, "My New Chapter", "robink");
			// Robin is not an author of the book
			Assert.fail();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Verify like feature
	 */
	@Test
	public void testListBooksByTitle() {
		List<Book> books = bookManager.listByTitle("Product");
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
	 * 
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
				.assertEquals(
						chapterOne.getContent(),
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><chapter xmlns=\"\" idStart=\"1\"><p id=\"0\">You will need ...</p></chapter>");
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
		Assert.assertTrue(chapterOne.getContent().contains(
				"<p id=\"0\">You will need ...</p>"));
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
	public void testChapterAdd() {

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
		chapterManager.updateContent(chapterThree,
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

		Author john = authorManager.findByUsername("john");
		Comment com = chapterManager.addComment(chapters.get(0), john,
				"This paragraph doesn't make sense.", "0");
		List<Comment> openComs = commentManager.listOpenForChapter(chapters
				.get(0).getId());

		Assert.assertNotNull(openComs);
		Assert.assertEquals(openComs.size(), 1,
				"There is at least one open comment.");
	}
}
