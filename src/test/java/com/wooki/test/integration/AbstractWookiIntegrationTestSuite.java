package com.wooki.test.integration;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.junit.Assert;

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
		Assert.assertTrue("Index page should be displayed instead of " + this.getText("//title"), this.getText("//title").equals(
				"wooki - Collaborative Writing"));
	}

	/**
	 * Verify that the returned page corresponds to a resource not found.
	 * 
	 * @param text
	 */
	protected void checkNotFound() {
		Assert.assertTrue("Resource should not be found but was " + this.getText("//title"), this.getText("//title").equalsIgnoreCase("Resource Not Found"));
	}

	/**
	 * Verify that the access to the corresponding has been denied for logged
	 * user.
	 */
	protected void checkAccessDenied() {
		Assert.assertTrue("Access to this resource should be denied instead of " + this.getText("//title"), this.getText("//title").equalsIgnoreCase("Access Denied"));
	}

	/**
	 * Check if the page returned is the signin page.
	 * 
	 */
	protected void checkSignin() {
		Assert.assertTrue("Access to this resource should be denied.", this.getText("//title").equals("Wooki Signin"));
	}

	/**
	 * Verify if the title contains profile names
	 */
	protected void checkProfile(String profile) {
		Assert.assertTrue("Page should be displaying the profile of " + profile, this.getText("//title").equals(profile + "'s Profile"));
	}
	
	/**
	 * Verify if the title contains dashboard owne name
	 */
	protected void checkDashboard(String username) {
		Assert.assertTrue("Page should be displaying the dashboard of " + username + " instead of " + this.getText("//title"), this.getText("//title").equals(username + "'s Dashboard"));
	}

	/**
	 * Check that the book title is correct.
	 * 
	 * @param title
	 */
	protected void checkBookTitle(String title) {
		Assert.assertTrue("Book title is incorrect " + this.getText("//title"), this.getText("//title").contains(title));
	}

	/**
	 * Verify chapter title
	 */
	protected void checkChapterTitle(String title) {
		Assert.assertTrue("Chapter title is incorrect " + this.getText("//title"), this.getText("//title").contains(title));
	}
	
	/**
	 * Verify book authors
	 */
	protected void checkAuthors(String... names) {
		Assert.assertTrue("Cannot find book meta: authors", isElementPresent("id=authors"));
		for (String name : names) {
			Assert.assertTrue("Author '" + name + "' was not found", this.getText("//p[@id='authors']").contains(name));
		}
	}
	
	/**
	 * Verify table of contents
	 */
	protected void checkTableOfContents(String... chapters) {
		Assert.assertTrue("Cannot find book meta: authors", this.getText("//div[@id='book']").contains("Table of contents"));
		for (String chapter : chapters) {
			Assert.assertTrue("Chapter '" + chapter + "' was not found", this.getText("//ol[@id='table-of-contents']").contains(chapter));
		}
	}

}
