package com.wooki.installer.services;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import com.wooki.core.services.CoreModule;

/**
 * Module for installation wizard application.
 */
@SubModule(CoreModule.class)
public class InstallerModule
{    
    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryConfiguration", new ClasspathResourceSymbolProvider(
                "config/installer.properties"));
    }
}
