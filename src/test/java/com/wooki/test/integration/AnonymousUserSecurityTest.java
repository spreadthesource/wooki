package com.wooki.test.integration;

import org.junit.Assert;
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
	 */
	@Test
	public void testDashboard() {
		open("/dashboard");
		waitForPageToLoad();
		checkSignin();
	}
	
}
