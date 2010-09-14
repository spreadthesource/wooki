package com.wooki.test;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.test.PageTester;
import org.springframework.mock.web.MockServletContext;

/**
 * This tester also import our Spring configuration into the tapestry registry.
 * 
 * @author ccordenier
 */
public class WookiPageTester extends PageTester
{

    private MockServletContext servletContext;

    public WookiPageTester(String appPackage, String appName, String contextPath,
            Class... moduleClasses)
    {
        super(appPackage, appName, contextPath, moduleClasses);
        Registry registry = this.getRegistry();
        servletContext.setAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME, registry);
    }

    public WookiPageTester(String appPackage, String appName)
    {
        super(appPackage, appName);
    }

    @Override
    protected ModuleDef[] provideExtraModuleDefs()
    {
        servletContext = new MockServletContext();
        servletContext.addInitParameter(
                "contextConfigLocation",
                "classpath*:applicationContext.xml");
        return new ModuleDef[]
        { new SpringModuleDef(servletContext) };
    }

}
