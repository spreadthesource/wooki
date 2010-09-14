package com.wooki.test.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Check if book navigation and rendering is ok : abstract, table of content, navigation between
 * chapters and so on. Checks are done on the initialy added book.
 */
public class BookNavigationTest extends AbstractWookiIntegrationTestSuite
{

    public final static String BOOK_TITLE = "The book of Wooki";

    /**
     * Check book index
     */
    @Test(enabled = true)
    public void bookIndex()
    {
        // opening book
        open("book/1");
        waitForPageToLoad();
        Assert.assertTrue(isElementPresent("id=book"), "Cannot load initial book");

        // checking title
        checkBookTitle(BOOK_TITLE);

        // checking authors
        checkAuthorsPresent(3);

        // checking abstract

        // checking table of contents
        checkTableOfContentsPresent(4);
    }

    @Test(enabled = true)
    public void checkChapters()
    {
        int bookId = 1;
        int chapters = 4;

        for (int chapterId = 2; chapterId < chapters; chapterId++)
        {
            open("chapter/" + bookId + "/" + chapterId);
            waitForPageToLoad();

            Assert.assertTrue(isElementPresent("id=content"), "Could not load chapter " + chapterId
                    + " book " + bookId);

            // check if there is a chapter title
            Assert.assertEquals(
                    1,
                    this.getXpathCount("//div[@id='content']//h2"),
                    "Could not find chapter title : "
                            + this.getXpathCount("//div[@id='content']//h2") + " h2 tag found");

            if (chapterId > 1)
                Assert.assertTrue(
                        isElementPresent("id=nav-left"),
                        "Could not find previous nav link for chapter " + chapterId + ", book "
                                + bookId);
            else
                Assert.assertFalse(
                        isElementPresent("id=nav-left"),
                        "unexpected found of previous nav link for chapter " + chapterId
                                + ", book " + bookId);

            if (chapterId < chapters)
                Assert.assertTrue(
                        isElementPresent("id=nav-right"),
                        "Could not find next nav link for chapter " + chapterId + ", book "
                                + bookId);
            else
                Assert.assertFalse(
                        isElementPresent("id=nav-right"),
                        "unexpected found of next nav link for chapter " + chapterId + ", book "
                                + bookId);
        }

    }

    @Test(enabled = true)
    public void checkIssues()
    {
        open("book/issues/1");
        waitForPageToLoad();

        int bookId = 1;
        int chapters = 4;

        Assert.assertTrue(isElementPresent("id=content"), "Could not load issues page");

        // check if there is a chapter title
        Assert.assertEquals(
                chapters,
                this.getXpathCount("//div[@id='content']//h3"),
                "Could not find exepected number of chapters : "
                        + this.getXpathCount("//div[@id='content']//h3") + " h3 tag found");

        for (int chapterId = 1; chapterId < chapters; chapterId++)
        {
            checkChapterPage("chapter/issues", bookId, chapterId);
        }

    }
}
