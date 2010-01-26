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
	}
	
	@Test(dependsOnMethods={"signup"})
	public void testIndex() {
		
	}
	
}
