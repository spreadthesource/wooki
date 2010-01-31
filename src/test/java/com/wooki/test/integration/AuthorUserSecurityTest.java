package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test will verify that an author user has access to all the required
 * elements to edit the book he is owning.
 *
 * @author ccordenier
 * 
 */
public class AuthorUserSecurityTest extends AbstractWookiIntegrationTestSuite {

	public AuthorUserSecurityTest() {
		super("src/main/webapp");
	}

	/**
	 * Register a new user, this is the first method to execute in the test.
	 * 
	 */
	@Test
	public void signin() {
		open("signin");
		Assert.assertTrue(isElementPresent("id=loginForm"), "Cannot load signin page");
		type("id=j_username", "robink");
		type("id=j_password", "password");
		click("//form[@id='loginForm']//input[@type='submit']");
		waitForPageToLoad();
		checkDashboard("robink");
	}

	/**
	 * Check index page.
	 */
	@Test(groups = { "author" }, dependsOnMethods = { "signin" })
	public void testIndex() {
		open("/book/1");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);
		Assert.assertTrue(isElementPresent("//a[@href='/book/1/last']"), "Working copy link is missing");
		Assert.assertTrue(isElementPresent("//a[@href='/chapter/edit/1/1']"), "Abstract edit link is missing");
		Assert.assertTrue(isElementPresent("//a[@href='/book/settings/1']"), "Abstract edit link is missing");
		Assert.assertTrue(isElementPresent("id=add-chapter-form"), "Add chapter link is missing");
	}

	/**
	 * Check that an author is authorized to change the state of a comment.
	 *
	 */
	@Test(groups = { "author" }, dependsOnMethods = { "signin" })
	public void testCommentPopup() {
		open("/book/1");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);

		// open comment popup
		click("id=c10");
		waitForCondition("var popupOpen = selenium.isElementPresent('id=ent-1'); popupOpen==true;", "10000");
		Assert.assertTrue(isElementPresent("//form[@action='/book/index.commentbubbles.commentdialogcontent.updatestateform/1']"), "Change comment state form is missing.");
	}

	
	/**
	 * Verify that the author has access to the working copy.
	 *
	 */
	@Test(groups = { "author" }, dependsOnMethods = { "signin" })
	public void testWorkingCopy() {
		open("/book/1/last");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);
	}
	
	/**
	 * Verify that the author has access to the edit page.
	 *
	 */
	@Test(groups = { "author" }, dependsOnMethods = { "signin" })
	public void testEditChapter() {
		open("/chapter/edit/1/1");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);
		Assert.assertTrue(isElementPresent("id=updateTitle"), "Missing update title form");
		Assert.assertTrue(isElementPresent("id=editChapterForm"), "Missing edit chapter form");
	}
	
	/**
	 * Verify that an author have access to links to modify and delete a chapter 
	 */
	@Test(groups = { "author" }, dependsOnMethods = { "signin" })
	public void testAdminChapter() {
		open("/chapter/1/1");
		waitForPageToLoad();
		checkChapterTitle("Abstract");
		Assert.assertTrue(isElementPresent("//a[@href='/chapter/edit/1/1']"), "Edit chapter link is missing");
		Assert.assertTrue(isElementPresent("//a[@href='/chapter/index:delete/1/1?t:ac=1/1']"), "Remove chapter link is missing.");
	}
	
}