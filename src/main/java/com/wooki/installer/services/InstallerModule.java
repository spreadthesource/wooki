package com.wooki.installer.services;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import com.spreadthesource.tapestry.installer.InstallerConstants;
import com.wooki.core.services.CoreModule;

/**
 * Module for installation wizard application.
 *
 * @author ccordenier
 *
 */
@SubModule(CoreModule.class)
public class InstallerModule
{

    /**
     * Contribute symbols values for installation application.
     *
     * @param configuration
     */
    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(InstallerConstants.INSTALLER_VERSION, "1.0");
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("/");
        
        String path = "";
        try
        {
            path = url.toURI().toString()  + "config/config.properties";
            path = path.substring(path.indexOf(":") + 1);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        
        configuration.add(InstallerConstants.CONFIGURATION_FILEPATH, path);
    }
    
    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryConfiguration", new ClasspathResourceSymbolProvider(
                "config/installer.properties"));
    }
    

}
