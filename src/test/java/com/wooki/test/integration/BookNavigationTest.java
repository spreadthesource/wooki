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
			checkChapterPage("chapter", bookId, chapterId);

			if (chapterId > 1)
				Assert.assertTrue("Could not find previous nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-left"));
			else
				Assert.assertFalse("unexpected found of previous nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-left"));

			if (chapterId < chapters)
				Assert.assertTrue("Could not find next nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-right"));
			else
				Assert.assertFalse("unexpected found of next nav link for chapter " + chapterId + ", book " + bookId, isElementPresent("id=nav-right"));
		}

	}

	@Test
	public void checkIssues() {
		open("chapter/issues/1/all");
		waitForPageToLoad();

		int bookId = 1;
		int chapters = 4;

		Assert.assertTrue("Could not load issues page", isElementPresent("id=content"));

		// check if there is a chapter title
		Assert.assertEquals("Could not find exepected number of chapters : " + this.getXpathCount("//div[@id='content']//h2") + " h2 tag found", chapters, this
				.getXpathCount("//div[@id='content']//h2"));

		for (int chapterId = 1; chapterId < chapters; chapterId++) {
			checkChapterPage("chapter/issues", bookId, chapterId);
		}

	}
}
