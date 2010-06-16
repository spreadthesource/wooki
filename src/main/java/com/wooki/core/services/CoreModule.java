package com.wooki.core.services;

import java.net.URL;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;

import com.spreadthesource.tapestry.installer.InstallerConstants;

public class CoreModule
{
    
    /**
     * Contribute symbols values for installation application.
     *
     * @param configuration
     */
    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(InstallerConstants.INSTALLER_VERSION, "1.0");
        
        //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = CoreModule.class.getResource("/");
        
        if (url == null) 
            throw new RuntimeException("Could not create path for configuration file");
        
        String path = "";
        path = url.toString()  + "config.properties";
        path = path.substring(path.indexOf(":") + 1);
        
        configuration.add(InstallerConstants.CONFIGURATION_FILEPATH, path);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("wooki", "com.wooki.core"));
    }
}
