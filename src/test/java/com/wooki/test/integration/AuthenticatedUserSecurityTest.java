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
		
		// Try to delete a book that exist but where john is not owner
		open("/dashboard:removebook/1");
		waitForPageToLoad();
		checkAccessDenied();
		
		// Not allowad context
		open("/dashboard/1");
		waitForPageToLoad();
		checkNotFound();
	}
	
}
