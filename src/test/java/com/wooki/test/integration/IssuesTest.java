package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Test behaviour on issues page, Abstract chapter is specific and only display on book front page.
 * 
 * @author ccordenier
 */
public class IssuesTest extends AbstractWookiIntegrationTestSuite
{

    @Test(enabled = true)
    public void signin()
    {
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
    @Test(enabled = true, dependsOnMethods =
    { "signin" })
    public void checkAbstractIssuesLink()
    {
        open("book/issues/1");
        waitForPageToLoad();
        Assert.assertTrue(isTextPresent("Introduction"));
        Assert.assertTrue(isTextPresent("Last Publication"));
        click("//div[@id='content']//h3[1]/a");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);

        open("book/issues/1");
        waitForPageToLoad();
        click("//div[@id='content']//ul[2]//a[2]");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);
    }

    /**
     * This test verifies that in the chapter issue page there is only on chapter displayed.
     */
    @Test(enabled = true, dependsOnMethods =
    { "signin" })
    public void testChapterIssues()
    {
        open("chapter/issues/1/1");
        waitForPageToLoad();
        assertEquals(getXpathCount("//div[@id='book']/h3"), 1);
    }

    /**
     * Verifies that the menu bar is correct.
     */
    @Test(enabled = true, dependsOnMethods =
    { "signin" })
    public void testMenuBar()
    {
        open("chapter/issues/1/1");
        waitForPageToLoad();
        assertEquals(
                getXpathCount("//a[@class='bookmenuitem selected']"),
                1,
                "Only one element should be selected in the menu");
        assertEquals(
                getText("//a[@class='bookmenuitem selected']"),
                "Comments",
                "Comments menu item should be selected in Comments page");
    }

    /**
     * Verify navigation links in chapter issues page
     */
    @Test(enabled = true, dependsOnMethods =
    { "signin" })
    public void testChapterIssuesNav()
    {
        open("chapter/issues/1/1");
        waitForPageToLoad();
        click("//li[@id='nav-left']/a[1]");
        waitForPageToLoad();
        assertEquals(getXpathCount("//div[@id='book']/h3"), 4);
    }
    
    /**
     * Simply check links and text are present
     */
    @Test(enabled = true, dependsOnMethods =
    { "signin" })
    public void testChapterIssuesLink()
    {
        // open issues page
        open("book/issues/1");
        waitForPageToLoad();
        Assert.assertTrue(isTextPresent("Introduction"));
        Assert.assertTrue(isTextPresent("Last Publication"));
        // Click on the link to last publication (default)
        click("//div[@id='content']//h3[2]/a");
        waitForPageToLoad();
        checkChapterTitle("Collaborative document publishing");

        // Click on the link to the revision
        open("book/issues/1");
        waitForPageToLoad();
        click("//div[@id='content']//ul[3]//a[2]");
        waitForPageToLoad();
        checkChapterTitle("Collaborative document publishing");
    }

    /**
     * Logout of the application.
     */
    @Override
    @AfterClass(alwaysRun = true)
    public void cleanup()
    {
        open("/index");
        waitForPageToLoad();
        Assert.assertTrue(
                isElementPresent("id=logout"),
                "Authenticated user should be able to logout");
        click("id=logout");
        waitForPageToLoad();
        checkIndex();
        super.cleanup();
    }

}
