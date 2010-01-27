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
		Assert.assertTrue("Cannot load initial book", isElementPresent("id=book"));

		// checking title
		checkBookTitle(BOOK_TITLE);

		// checking authors
		checkAuthors("robink", "ccordenier", "bverachten");

		// checking abstract
		
		// checking table of contents
		checkTableOfContents("Collaborative document publishing","Open source contribution","Get started");
	}
}
