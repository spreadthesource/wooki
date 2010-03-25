package com.wooki.test.integration;

import org.testng.annotations.Test;

/**
 * Test more link against a list that stop to have more elements after 4 iterations.
 * 
 * @author ccordenier
 */
public class TestMoreLink extends AbstractWookiIntegrationTestSuite
{

    @Test
    public void testMoreLink()
    {
        open("dev/MoreLinkTest");
        waitForPageToLoad("3000");

        for (int i = 0; i <= 3; i++)
        {
            click("id=morelinktest");
            waitForCondition("selenium.isElementPresent('id=elt" + (i * 10) + "')", "1000");
        }

        // Last click should remove the link
        click("id=morelinktest");
        waitForCondition("!selenium.isElementPresent('id=more')", "1000");
    }
}
