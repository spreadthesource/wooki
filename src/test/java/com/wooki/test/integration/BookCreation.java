package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Fill a complete book creation process.
 * 
 * @author ccordenier
 */
public class BookCreation extends AbstractWookiIntegrationTestSuite
{

    @Test(enabled = true)
    public void signup()
    {
        open("signup");
        assertTrue(isElementPresent("id=signupForm"), "Cannot load signup page");
        type("id=username", "songwriter");
        type("id=fullname", "Song Writer");
        type("id=email", "song.writer@gmail.com");
        type("id=password", "mylongpassword");
        type("id=fcaptcha", "i8cookies");
        click("//form[@id='signupForm']//input[@type='submit']");
        waitForPageToLoad();
        checkDashboard("songwriter");
    }

    /**
     * Create a book and verify TOC on index page.
     */
    @Test(enabled = true, dependsOnMethods =
    { "signup" })
    public void createBook()
    {
        type("id=bookTitle", "MySongBook");
        click("//form[@id='createBookForm']//input[@type='submit']");
        waitForPageToLoad();
        checkBookTitle("MySongBook");

        // Introduction chapter is created by default but is empty and not published
        assertEquals(
                getXpathCount("//ol[@id='table-of-contents']/li"),
                1,
                "Chapter 'Introduction' seems to be missing");
        assertEquals("Introduction", getText("//span[@class='inactive']"));
        assertEquals("Book Index", getText("//a[@class='bookmenuitem selected']"));
    }

    /**
     * Add a chapter and wait to see it in the Toc
     */
    @Test(enabled = true, dependsOnMethods =
    { "createBook" })
    public void addChapter()
    {
        click("id=showAddChapterField");
        type("id=chapterName", "I am singing in the rain");
        click("//form[@id='addChapterForm']//input[@type='submit']");
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        refresh();
        waitForPageToLoad();
        assertEquals(
                getXpathCount("//ol[@id='table-of-contents']/li"),
                2,
                "Chapter has not been added");
    }

    /**
     * Add a chapter and wait to see it in the Toc
     */
    @Test(enabled = true, dependsOnMethods =
    { "addChapter" })
    public void bookSettings()
    {
        click("//ul[@id='adminMenu']//a[1]");
        waitForPageToLoad();
        assertEquals("Book Settings", getText("//a[@class='bookmenuitem selected']"));

        type("id=abstract", "Book of my songs");
        click("//form[@id='bookInfoForm']//input[@type='submit']");
        waitForPageToLoad();

        click("//li[@id='book-root']//a[1]");
        waitForPageToLoad();

        assertTrue(isTextPresent("Abstract"));
        assertTrue(isTextPresent("Book of my songs"));
    }

    /**
     * Edit and publish intro.
     */
    @Test(enabled = true, dependsOnMethods =
    { "bookSettings" })
    public void editIntoduction()
    {
        click("//ol[@id='table-of-contents']/li[1]//a[1]");
        waitForPageToLoad();
        assertEquals(getText("//a[@class='bookmenuitem selected']").toLowerCase(), "edit current");

        click("id=publish");
        waitForPageToLoad();
        assertEquals(getText("//a[@class='bookmenuitem selected']").toLowerCase(), "chapter index");
    }

    /**
     * Edit and publish intro.
     */
    @Test(enabled = true, dependsOnMethods =
    { "editIntoduction" })
    public void checkConfirmOnEditLeave()
    {
        click("//ul[@id='adminMenu']//a[1]");
        waitForPageToLoad();

        click("id=update");
        waitForPageToLoad();
        assertEquals("chapter index", getText("//a[@class='bookmenuitem selected']").toLowerCase());
    }

    /**
     * Check that we have
     */
    @Test(enabled = true, dependsOnMethods =
    { "checkConfirmOnEditLeave" })
    public void checkHistory()
    {
        click("//a[starts-with(@href, '/chapter/history/')]");
        waitForPageToLoad();
        assertEquals(
                getXpathCount("//ul[@id='revision-history']/li"),
                2,
                "There must be two lines in the history page, one draft and one publication");
    }

    /**
     * Delete introduction chapter and go back to the TOC.
     */
    @Test(enabled = true, dependsOnMethods =
    { "checkHistory" })
    public void deleteChapter()
    {
        refresh();
        waitForPageToLoad();

        chooseOkOnNextConfirmation();
        click("//a[contains(@href, 'delete')]");
        getConfirmation();
        waitForPageToLoad();

        assertEquals(
                getXpathCount("//ol[@id='table-of-contents']/li"),
                1,
                "Chapter 'Introduction' should have been removed");
    }

    /**
     * Delete the chapter on the user dashboard.
     */
    @Test(enabled = true, dependsOnMethods =
    { "deleteChapter" })
    public void deleteBook()
    {
        open("/dashboard");
        waitForPageToLoad();
        checkDashboard("songwriter");

        chooseOkOnNextConfirmation();
        click("//a[contains(@href,'removebook')]");
        getConfirmation();
        waitForPageToLoad();

        checkDashboard("songwriter");
        assertFalse(isTextPresent("MySongBook"));
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
