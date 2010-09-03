package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * This test will verify that an author user has access to all the required elements to edit the
 * book he is owning.
 * 
 * @author ccordenier
 */
public class AuthorUserSecurityTest extends AbstractWookiIntegrationTestSuite
{

    /**
     * Login with an existing user, this is the first method to execute in the test.
     */
    @Test
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
     * Check index page.
     */
    @Test(dependsOnMethods =
    { "signin" })
    public void testIndex()
    {
        open("/book/1");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);
        Assert.assertTrue(
                isElementPresent("//a[@href='/chapter/1/1/last']"),
                "Working copy link is missing");
        Assert.assertTrue(
                isElementPresent("//a[@href='/book/settings/1']"),
                "Book settings link is missing");
        Assert.assertTrue(isElementPresent("id=add-chapter-form"), "Add chapter link is missing");
    }

    /**
     * Check that an author is authorized to change the state of a comment.
     */
    @Test(dependsOnMethods =
    { "signin" })
    public void testCommentPopup()
    {
        open("/chapter/1/1");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);

        // open comment popup
        click("id=c10");
        waitForCondition(
                "var popupOpen = selenium.isElementPresent('id=ent-2'); popupOpen==true;",
                "10000");
        Assert
                .assertTrue(
                        isElementPresent("//form[@action='/chapter/index.commentbubbles.commentdialogcontent.updatestateform/2']"),
                        "Change comment state form is missing.");
    }

    /**
     * Verify that the author has access to the working copy.
     */
    @Test(dependsOnMethods =
    { "signin" })
    public void testWorkingCopy()
    {
        open("/chapter/1/1/last");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);
    }

    /**
     * Verify that the author has access to the edit page.
     */
    @Test(dependsOnMethods =
    { "signin" })
    public void testEditChapter()
    {
        open("/chapter/edit/1/1");
        waitForPageToLoad();
        checkBookTitle(BookNavigationTest.BOOK_TITLE);
        Assert.assertTrue(isElementPresent("id=updateTitle"), "Missing update title form");
        Assert.assertTrue(isElementPresent("id=editChapterForm"), "Missing edit chapter form");
    }

    /**
     * Verify that an author have access to links to modify and delete a chapter
     */
    @Test(dependsOnMethods =
    { "signin" })
    public void testAdminChapter()
    {
        open("/chapter/1/2");
        waitForPageToLoad();
        checkChapterTitle("Collaborative document publishing");
        Assert.assertTrue(
                isElementPresent("//a[@href='/chapter/edit/1/2']"),
                "Edit chapter link is missing");
        Assert.assertTrue(
                isElementPresent("//a[contains(@href,':delete?t:ac=1/2')]"),
                "Remove chapter link is missing.");
    }

    /**
     * Logout of the application.
     */
    @AfterClass(alwaysRun = true)
    @Override
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
