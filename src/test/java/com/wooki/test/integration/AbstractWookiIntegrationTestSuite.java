package com.wooki.test.integration;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.testng.Assert;

public class AbstractWookiIntegrationTestSuite extends AbstractIntegrationTestSuite {

	/**
	 * Default browser used to launch tests.
	 * 
	 */
	private static String defaultBrowser;

	// Set a default browser in function of the OS.
	static {
		String os = System.getProperty("os.name");
		if (os.contains("Mac OS")) {
			defaultBrowser = "*safari";
		} else {
			if (os.contains("Windows")) {
				defaultBrowser = "*iexplore";
			} else {
				defaultBrowser = "*firefox";
			}
		}
	}

	public AbstractWookiIntegrationTestSuite(String webAppRoot, String browserCommand, String... virtualHosts) {
		super(webAppRoot, browserCommand, virtualHosts);
	}

	public AbstractWookiIntegrationTestSuite(String webAppRoot) {
		this(webAppRoot, defaultBrowser);
	}

	/**
	 * Verify that the returned page corresponds to a resource not found.
	 * 
	 * @param text
	 */
	protected void checkIndex() {
		Assert.assertTrue(this.getText("//title").equals("wooki - Collaborative Writing"), "Index page should be displayed instead of "
				+ this.getText("//title"));
	}

	/**
	 * Verify that the returned page corresponds to a resource not found.
	 * 
	 * @param text
	 */
	protected void checkNotFound() {
		Assert.assertTrue(this.getText("//title").equalsIgnoreCase("Resource Not Found"), "Resource should not be found but was " + this.getText("//title"));
	}

	/**
	 * Verify that the access to the corresponding has been denied for logged
	 * user.
	 */
	protected void checkAccessDenied() {
		Assert.assertTrue(this.getText("//title").equalsIgnoreCase("Access Denied"), "Access to this resource should be denied instead of "
				+ this.getText("//title"));
	}

	/**
	 * Check if the page returned is the signin page.
	 * 
	 */
	protected void checkSignin() {
		Assert.assertTrue(this.getText("//title").equals("Wooki Signin"), "You should have been redirected to signin instead of " + this.getText("//title"));
	}

	/**
	 * Verify if the title contains profile names
	 */
	protected void checkProfile(String profile) {
		Assert.assertTrue(this.getText("//title").equals(profile + "'s Profile"), "Page should be displaying the profile of " + profile + " instead of "
				+ this.getText("//title"));
	}

	/**
	 * Verify if the title contains dashboard owne name
	 */
	protected void checkDashboard(String username) {
		Assert.assertTrue(this.getText("//title").equals(username + "'s Dashboard"), "Page should be displaying the dashboard of " + username + " instead of "
				+ this.getText("//title"));
	}

	/**
	 * Verify if the title contains dashboard owne name
	 */
	protected void checkAccountSettings(String username) {
		Assert.assertTrue(this.getText("//title").equals(username + "'s Settings"), "Page should be displaying the settings of " + username + " instead of "
				+ this.getText("//title"));
	}

	/**
	 * Check that the book title is correct.
	 * 
	 * @param title
	 */
	protected void checkBookTitle(String title) {
		Assert.assertTrue(this.getText("//title").contains(title), "Book title is incorrect " + this.getText("//title"));
	}

	/**
	 * Verify chapter title
	 */
	protected void checkChapterTitle(String title) {
		Assert.assertTrue(this.getText("//title").contains(title), "Chapter title is incorrect " + this.getText("//title"));
	}

	/**
	 * Verify book authors
	 */
	protected void checkAuthorsPresent(int number) {
		Assert.assertTrue(isElementPresent("id=authors"), "Cannot find book meta: authors");
		Assert.assertEquals(this.getXpathCount("//p[@id='authors']//a"), number);
	}

	/**
	 * Verify table of contents
	 */
	protected void checkTableOfContentsPresent(int number) {
		Assert.assertTrue(this.getText("//div[@id='book']").contains("Table of contents"), "Cannot find Table of contents");
		Assert.assertEquals(this.getXpathCount("//ol[@id='table-of-contents']//li"), number);
	}

	/**
	 * Check if a chapter page has been loaded
	 * 
	 * @param bookId
	 * @param chapterId
	 */
	public void checkChapterPage(String prefix, int bookId, int chapterId) {
		open(prefix + "/" + bookId + "/" + chapterId);
		waitForPageToLoad();

		Assert.assertTrue(isElementPresent("id=content"), "Could not load chapter " + chapterId + " book " + bookId);

		// check if there is a chapter title
		Assert.assertEquals(1, this.getXpathCount("//div[@id='content']//h2"), "Could not find chapter title : "
				+ this.getXpathCount("//div[@id='content']//h2") + " h2 tag found");
	}

}
