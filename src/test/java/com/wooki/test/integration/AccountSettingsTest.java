package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * This test class is design to check that URL are secured by Wooki security
 * configuration in the case an authenticated user access to unauthorized
 * resources.
 * 
 * @author ccordenier
 * 
 */
public class AccountSettingsTest extends AbstractWookiIntegrationTestSuite {

	/**
	 * Register a new user, this is the first method to execute in the test.
	 * 
	 */
	@Test
	public void signup() {
		open("signup");
		Assert.assertTrue(isElementPresent("id=signupForm"), "Cannot load signup page");
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
	@Test(dependsOnMethods = { "signup" })
	public void testUpdatePasswordFailure() {

		open("/accountSettings");
		waitForPageToLoad();
		type("id=oldPassword", "wrongpassword");
		type("id=newPassword", "newpassword");
		type("id=newPasswordConfirmation", "newpassword");
		click("//form[@id='passwordChange']//input[@type='submit']");
		waitForPageToLoad();
		Assert.assertTrue(isTextPresent("Your old password is incorrect"), "An error message should be displayed with incorrect old password.");

	}

	@Test(dependsOnMethods = { "signup" })
	public void testUpdatePasswordSuccess() {

		open("/accountSettings");
		waitForPageToLoad();
		type("id=oldPassword", "mylongpassword");
		type("id=newPassword", "newpassword");
		type("id=newPasswordConfirmation", "newpassword");
		click("//form[@id='passwordChange']//input[@type='submit']");
		waitForPageToLoad();
		Assert.assertTrue(isTextPresent("Your password has been updated"), "An flash message should be displayed to inform password change success.");

	}

	/**
	 * Logout of the application.
	 * 
	 */
	@AfterClass(alwaysRun = true)
	@Override
	public void cleanup() throws Exception {
		open("/index");
		waitForPageToLoad();
		Assert.assertTrue(isElementPresent("id=logout"), "Authenticated user should be able to logout");
		click("id=logout");
		waitForPageToLoad();
		checkIndex();
		super.cleanup();
	}

}
