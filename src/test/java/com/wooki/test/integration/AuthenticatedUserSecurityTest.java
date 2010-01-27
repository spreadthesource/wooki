package com.wooki.test.integration;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * This test class is design to check that URL are secured by Wooki security
 * configuration in the case an authenticated user access to unauthorized
 * resources.
 * 
 * @author ccordenier
 * 
 */
public class AuthenticatedUserSecurityTest extends AbstractWookiIntegrationTestSuite {

	public AuthenticatedUserSecurityTest() {
		super("src/main/webapp");
	}

	/**
	 * Register a new user, this is the first method to execute in the test.
	 * 
	 */
	@Test
	public void signup() {
		open("signup");
		Assert.assertTrue("Cannot load signup page", isElementPresent("id=signupForm"));
		type("id=username", "john");
		type("id=fullname", "John Doe");
		type("id=email", "John.Doe@gmail.com");
		type("id=password", "mylongpassword");
		click("//form[@id='signupForm']//input[@type='submit']");
		waitForPageToLoad();
		checkDashboard("john");
	}

	/**
	 * Check index page.
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testIndex() {
		open("/");
		waitForPageToLoad();
		checkProfile("john");

		open("/index");
		waitForPageToLoad();
		checkProfile("john");

		open("/ccordenier");
		waitForPageToLoad();
		checkProfile("ccordenier");

		open("/1/2");
		waitForPageToLoad();
		checkNotFound();

		open("/userNotExist");
		waitForPageToLoad();
		checkNotFound();
	}

	/**
	 * Test access to dashboard.
	 * 
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testDashboard() {
		open("/dashboard");
		waitForPageToLoad();
		checkDashboard("john");

		// Try to delete a book that exist but that john does not own
		open("/dashboard:removebook/1");
		waitForPageToLoad();
		checkAccessDenied();

		// Not allowad context
		open("/dashboard/1");
		waitForPageToLoad();
		checkNotFound();
	}

	/**
	 * Test access to dashboard.
	 * 
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testAccountSettings() {
		open("/accountSettings");
		waitForPageToLoad();
		checkAccountSettings("john");

		// Bad URL
		open("/accountSettings/1");
		waitForPageToLoad();
		checkNotFound();
	}

	/**
	 * Verify that the user cannot delete a comment when he is not the owner
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testComment() {
		open("/book/index.commentbubbles.commentdialogcontent.clickandremove:clickandremove/1?t:ac=1");
		checkAccessDenied();
	}

	/**
	 * Verify that elements are not present if the user is not author of the
	 * book.
	 * 
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testBookIndex() {
		open("/book/1");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);
		Assert.assertFalse("Admin button should not be present for this book", isElementPresent("id='book-admin'"));
		Assert.assertFalse("Admin button should not be present for this book", isElementPresent("id='add-chapter-link'"));

		// User is not owner so he has not access to the last copy
		open("/book/1/last");
		waitForPageToLoad();
		checkAccessDenied();
	}

	/**
	 * When the user is authenticated, it should not be able to see signin.
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testSignin() {
		open("/signin");
		waitForPageToLoad();
		checkProfile("john");
	}

	/**
	 * When the user is authenticated, it should not be able to see signup.
	 */
	@Test(groups = { "authenticated" }, dependsOnMethods = { "signup" })
	public void testSignup() {
		open("/signup");
		waitForPageToLoad();
		checkProfile("john");
	}

	/**
	 * Logout of the application.
	 *
	 */
	@Test(dependsOnGroups = { "authenticated" })
	public void testLogout() {
		open("/index");
		waitForPageToLoad();
		Assert.assertTrue("Authenticated user should be able to logout", isElementPresent("id=logout"));
		click("id=logout");
		waitForPageToLoad();
		checkIndex();
	}
}
