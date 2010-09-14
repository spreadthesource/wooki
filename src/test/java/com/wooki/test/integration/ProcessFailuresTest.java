package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Use this to test application failures.
 * 
 * @author ccordenier
 */
public class ProcessFailuresTest extends AbstractWookiIntegrationTestSuite
{

    /**
     * Register a new user, this is the first method to execute in the test.
     */
    @Test(enabled = true)
    public void signup()
    {
        open("/signup");
        waitForPageToLoad();
        Assert.assertTrue(isElementPresent("id=signupForm"), "Cannot load signup page");
        type("id=username", "WrongCaptcha");
        type("id=fullname", "Wrong Captcha");
        type("id=email", "WrongCaptcha@gmail.com");
        type("id=password", "mylongpassword");
        type("id=fcaptcha", "wrong");
        click("//form[@id='signupForm']//input[@type='submit']");
        waitForPageToLoad();
        Assert.assertTrue(isTextPresent("Enter the text displayed in the image."));
    }

}
