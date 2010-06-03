package com.wooki.test.unit;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.test.PageTester;
import org.springframework.context.ApplicationContext;

import com.spreadthesource.tapestry.installer.services.InstallerModule;
import com.wooki.services.WookiModule;

/**
 * This class is in charge of initializing Tapestry for the wooki application. So, concrete test
 * suite will have access to all the Tapestry services and Spring beans.
 * 
 * @author ccordenier
 */
public class AbstractWookiUnitTestSuite
{

    /**
     * Use this page tester to unit test pages.
     */
    protected PageTester pageTester;

    /**
     * Use this registry to get access to Tapestry services
     */
    protected Registry registry;

    /**
     * Use this context to get access to Spring beans.
     */
    protected ApplicationContext context;

    public AbstractWookiUnitTestSuite()
    {
        pageTester = new WookiPageTester("com.wooki", "wooki", "src/main/webapp",
                WookiModule.class, com.wooki.installer.services.InstallerModule.class,
                InstallerModule.class);
        registry = pageTester.getRegistry();
        context = registry.getService(ApplicationContext.class);
    }

}
