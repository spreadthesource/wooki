package com.wooki.test.integration;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Check if book navigation and rendering is ok : abstract, table of content,
 * navigation between chapters and so on. Checks are done on the initialy added
 * book.
 */
public class BookNavigationTest extends AbstractWookiIntegrationTestSuite {

	public final static String BOOK_TITLE = "The book of Wooki";

	public BookNavigationTest() {
		super("src/main/webapp");
	}

	/**
	 * Check book index
	 */
	@Test
	public void bookIndex() {
		// opening book
		open("book/1");
		waitForPageToLoad();
		Assert.assertTrue("Cannot load initial book", isElementPresent("id=book"));

		// checking title
		checkBookTitle(BOOK_TITLE);

		// checking authors
		checkAuthorsPresent(3);

		// checking abstract

		// checking table of contents
		checkTableOfContentsPresent(3);
	}

	@Test
	public void checkChapters() {
		int bookId = 1;
		int chapters = 4;

		for (int chapterId = 1; chapterId < chapters; chapterId++) {
			checkChapter(bookId, chapterId);
			
			if(chapterId > 1) 
				Assert.assertTrue("Could not find previous nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-left"));
			else
				Assert.assertFalse("unexpected found of previous nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-left"));
			
			if(chapterId < chapters) 
				Assert.assertTrue("Could not find next nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-right"));
			else
				Assert.assertFalse("unexpected found of next nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-right"));
		}

	}

	public void checkChapter(int bookId, int chapterId) {
		open("chapter/" + bookId + "/" + chapterId);
		waitForPageToLoad();

		// check book layout
		Assert.assertTrue("Could not load chapter " + chapterId + " book " + bookId, isElementPresent("id=book"));

		// check if there is a chapter title
		Assert.assertEquals("Could not find chapter title : " + this.getXpathCount("//div[@id='book']//h2") + " h2 tag found", this
				.getXpathCount("//div[@id='book']//h2"), 1);
	}
}
