package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test class is design to check that URL are secured by Wooki security
 * configuration.
 * 
 * @author ccordenier
 * 
 */
public class AnonymousUserSecurityTest extends AbstractWookiIntegrationTestSuite {

	/**
	 * Verify different access type to index page
	 */
	@Test
	public void testIndexPage() {

		open("/ccordenier");
		waitForPageToLoad();
		checkProfile("ccordenier");

		// Too many parameters
		open("/ccordenier/1");
		waitForPageToLoad();
		checkNotFound();

		// Inexistant user
		open("/userNotExist");
		waitForPageToLoad();
		checkNotFound();

	}

	/**
	 * Try to access to account settings page.
	 */
	@Test
	public void testAccountSettings() {
		// Verify redirection to signin page
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
		// Verify redirection to signin page
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
		// Verify redirection to signin pages
		open("/book/settings/1");
		waitForPageToLoad();
		checkSignin();
	}

	/**
	 * Try to access to book settings page.
	 * 
	 */
	@Test
	public void testBookAccess() {
		open("/book/1");
		waitForPageToLoad();
		checkBookTitle("The book of Wooki");

		open("/book/index/1");
		waitForPageToLoad();
		checkBookTitle("The book of Wooki");

		open("/book/index/2");
		waitForPageToLoad();
		checkNotFound();

		open("/book/index/1/last");
		waitForPageToLoad();
		checkAccessDenied();

		open("/book/index/1/2/3");
		waitForPageToLoad();
		checkNotFound();
	}

	/**
	 * Try to access to book settings page.
	 * 
	 */
	@Test
	public void testEditChapter() {

		open("/chapter/edit/1/1");
		waitForPageToLoad();
		checkSignin();

		// Resource does not exist
		open("/chapter/edit/2/2");
		waitForPageToLoad();
		checkSignin();

		// Incorrect parameters
		open("/chapter/edit/2");
		waitForPageToLoad();
		checkSignin();
	}

	/**
	 * Test chapter access.
	 * 
	 */
	@Test
	public void testChapterAccess() {
		// Access an existing chapter
		open("/chapter/1/2");
		waitForPageToLoad();
		Assert.assertTrue(isElementPresent("id=nav-left"));
		Assert.assertTrue(isElementPresent("id=nav-right"));
		Assert.assertFalse(isElementPresent("id=book-admin"));
		checkChapterTitle("Collaborative document publishing");

		// Resource does not exist
		open("/chapter/1/5");
		waitForPageToLoad();
		checkNotFound();

		// Resource does not exist
		open("/chapter/5/1");
		waitForPageToLoad();
		checkNotFound();

		// Revision does not exist
		open("/chapter/1/1/4");
		waitForPageToLoad();
		checkNotFound();

	}

}
