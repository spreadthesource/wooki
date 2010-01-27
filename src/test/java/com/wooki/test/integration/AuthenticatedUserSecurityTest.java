package com.wooki.test.integration;

import org.junit.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.wooki.pages.Signin;

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
	 * Register a new user.
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
	
	@Test(dependsOnMethods={"signup"})
	public void testIndex() {
		open("/");
		waitForPageToLoad();
		checkProfile("john");

		open("/index");
		waitForPageToLoad();
		checkProfile("john");
	}
	
}
