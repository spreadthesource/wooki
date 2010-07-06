package com.wooki.installer.services;

import java.util.Properties;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.services.MigrationModule;
import com.spreadthesource.tapestry.installer.InstallerConstants;
import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.spreadthesource.tapestry.installer.services.ConfigurationTask;
import com.wooki.core.services.CoreModule;

/**
 * Module for installation wizard application.
 */
@SubModule(
{ CoreModule.class, MigrationModule.class })
public class InstallerModule
{

    public void contributeApplicationDefaults(MappedConfiguration<String, String> conf)
    {
        conf.add(MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION, "false");
        conf.add(InstallerConstants.SILENT_MODE, "true");
    }

    /**
     * Link migration helper to application settings service.
     * 
     * @param configurers
     * @param settings
     */
    public void contributeDbSource(OrderedConfiguration<HibernateConfigurer> configurers,
            final ApplicationSettings settings)
    {
        configurers.add("AppSettings", new HibernateConfigurer()
        {

            public void configure(org.hibernate.cfg.Configuration configuration)
            {
                configuration.setProperties(new Properties()
                {
                    @Override
                    public String getProperty(String key)
                    {
                        return settings.get(key);
                    }

                });

            }
        });
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(GlobalSettingsTask.class, GlobalSettingsTaskImpl.class);
    }

    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryConfiguration", new ClasspathResourceSymbolProvider(
                "config/installer.properties"));
    }

    public void contributeMigrationManager(Configuration<String> configuration)
    {
        configuration.add("com.wooki.installer.schema");
    }

    public static void contributeConfigurationManager(
            OrderedConfiguration<ConfigurationTask> configuration,
            @Inject GlobalSettingsTask settings)
    {
        configuration.add("WookiConf", settings, "before:*");
    }
}
