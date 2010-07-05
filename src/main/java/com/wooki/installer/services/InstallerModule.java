package com.wooki.installer.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import com.spreadthesource.tapestry.installer.InstallerConstants;
import com.spreadthesource.tapestry.installer.services.ConfigurationTask;
import com.wooki.core.services.CoreModule;

/**
 * Module for installation wizard application.
 */
@SubModule(CoreModule.class)
public class InstallerModule
{
    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(InstallerConstants.SILENT_MODE, "true");
    }
    
    public static void bind(ServiceBinder binder)
    {
        binder.bind(GlobalSettingsTask.class, GlobalSettingsTaskImpl.class);
        //binder.bind(MigrationTask.class, MigrationTaskImpl.class);
    }

    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryConfiguration", new ClasspathResourceSymbolProvider(
                "config/installer.properties"));
    }

    public static void contributeConfigurationManager(
            OrderedConfiguration<ConfigurationTask> configuration,
            @Inject GlobalSettingsTask settings)
    {
        configuration.add("WookiConf", settings, "before:*");
        //configuration.add("Migration", migration, "before:Terminator,after:WookiConf");
    }
}
