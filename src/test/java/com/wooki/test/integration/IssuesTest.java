package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test behaviour on issues page, Abstract chapter is specific and only display on book front page.
 * 
 * @author ccordenier
 * 
 */
public class IssuesTest extends AbstractWookiIntegrationTestSuite {

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
	 * Simply check links and text are present
	 */
	@Test(dependsOnMethods = { "signin" })
	public void checkAbstractIssuesLink() {
		open("chapter/issues/1/all");
		waitForPageToLoad();
		Assert.assertTrue(isTextPresent("Abstract"));
		Assert.assertTrue(isTextPresent("Last Publication"));
		click("//div[@id='content']//h3[1]/a");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);

		open("chapter/issues/1/all");
		waitForPageToLoad();
		click("//div[@id='content']//ul[1]//a[2]");
		waitForPageToLoad();
		checkBookTitle(BookNavigationTest.BOOK_TITLE);		
	}

	/**
	 * Simply check links and text are present
	 */
	@Test(dependsOnMethods = { "signin" })
	public void checkChapterIssuesLink() {
		
		// open issues page
		open("chapter/issues/1/all");
		waitForPageToLoad();
		Assert.assertTrue(isTextPresent("Abstract"));
		Assert.assertTrue(isTextPresent("Last Publication"));
		
		// Click on the link to last publication (default)
		click("//div[@id='content']//h3[2]/a");
		waitForPageToLoad();
		checkChapterTitle("Collaborative document publishing");

		// Click on the link to the revision
		open("chapter/issues/1/all");
		waitForPageToLoad();
		click("//div[@id='content']//ul[2]//a[2]");
		waitForPageToLoad();
		checkChapterTitle("Collaborative document publishing");
		
	}
	
}
