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
	protected void checkNotFound() {
		Assert.assertTrue("Resource should not be found.", this.getText("//title").equals("Resource Not Found"));
	}

	/**
	 * Verify that the access to the corresponding has been denied for logged
	 * user.
	 */
	protected void checkAccessDenied() {
		Assert.assertTrue("Access to this resource should be denied.", this.getText("//title").equals("Access Denied"));
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
	 * Check that the book title is correct.
	 *
	 * @param title
	 */
	protected void checkBookTitle(String title) {
		Assert.assertTrue("Book title is incorrect", this.getText("//title").contains(title));
	}
	
}
