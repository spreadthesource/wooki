package com.wooki.test.unit;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.test.PageTester;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.spreadthesource.tapestry.dbmigration.services.MigrationManager;
import com.spreadthesource.tapestry.installer.services.InstallerModule;
import com.wooki.app0.services.AppModule;
import com.wooki.app0.services.UnitTestModule;
import com.wooki.services.WookiModule;
import com.wooki.test.WookiPageTester;

/**
 * This class is in charge of initializing Tapestry for the wooki application. So, concrete test
 * suite will have access to all the Tapestry services and Spring beans.
 * 
 * @author ccordenier
 */
@Test(sequential = true)
public abstract class AbstractWookiUnitTestSuite
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

    @BeforeClass
    public void setup()
    {
        pageTester = new WookiPageTester("com.wooki", "wooki", "src/main/webapp",
                WookiModule.class, com.wooki.installer.services.InstallerModule.class,
                InstallerModule.class, UnitTestModule.class, AppModule.class);
        registry = pageTester.getRegistry();
        context = registry.getService(ApplicationContext.class);

        MigrationManager manager = pageTester.getService(MigrationManager.class);
        manager.initialize();
        manager.reset();
        manager.migrate();
    }

    @AfterClass
    public void cleanup()
    {
        if (registry != null)
        {
            MigrationManager manager = pageTester.getService(MigrationManager.class);
            manager.reset();

            registry.shutdown();
        }
    }

}
