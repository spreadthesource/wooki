package com.wooki.test.integration;

import org.testng.annotations.Test;

/**
 * This test class is design to check that URL are secured by Wooki security
 * configuration.
 * 
 * @author ccordenier
 * 
 */
public class AnonymousUserSecurityTest extends AbstractWookiIntegrationTestSuite {

	public AnonymousUserSecurityTest() {
		super("src/main/webapp");
	}

	/**
	 * Verify different access type to index page
	 */
	@Test
	public void testIndexPage() {

		open("/ccordenier");
		waitForPageToLoad();
		checkProfile("ccordenier");

		open("/ccordenier/1");
		waitForPageToLoad();
		checkNotFound();

		open("/userNotExist");
		waitForPageToLoad();
		checkNotFound();

	}

	/**
	 * Try to access to account settings page.
	 */
	@Test
	public void testAccountSettings() {
		open("/accountSettings");
		waitForPageToLoad();
		checkSignin();
	}

	/**
	 * Try to access to account settings page.
	 * 
	 */
	@Test
	public void testDashboard() {
		open("/dashboard");
		waitForPageToLoad();
		checkSignin();
	}

	/**
	 * Try to access to book settings page.
	 * 
	 */
	@Test
	public void testBookSettings() {
		open("book/settings/1");
		waitForPageToLoad();
		checkSignin();
	}

	/**
	 * Try to access to book settings page.
	 * 
	 */
	@Test
	public void testBookAccess() {
		open("book/1");
		waitForPageToLoad();
		checkBookTitle("The book of Wooki");

		open("book/index/1");
		waitForPageToLoad();
		checkBookTitle("The book of Wooki");
		
		open("book/index/2");
		waitForPageToLoad();
		checkIndex();
	}
	
	/**
	 * Try to access to book settings page.
	 * 
	 */
	@Test
	public void testEditChapter() {
		open("chapter/edit/1/1");
		waitForPageToLoad();
		checkSignin();

		// Resource does not exist
		open("chapter/edit/2/2");
		waitForPageToLoad();
		checkSignin();

		// Incorrect parameters
		open("chapter/edit/2");
		waitForPageToLoad();
		checkSignin();
	}
	
	/**
	 * Test chapter access.
	 * 
	 */
	@Test
	public void testChapterAccess() {
		open("chapter/1/2");
		waitForPageToLoad();
		checkChapterTitle("Collaborative document publishing");

		// Resource does not exist
		open("chapter/1/5");
		waitForPageToLoad();
		checkBookTitle("The book of Wooki");

		// Resource does not exist
		open("chapter/5/1");
		waitForPageToLoad();
		checkIndex();

		// Revision does not exist
		open("chapter/1/1/5");
		waitForPageToLoad();
		checkNotFound();

	}
	
}
