package com.wooki.installer.services;

import org.apache.tapestry5.ioc.MappedConfiguration;

import com.spreadthesource.tapestry.installer.InstallerConstants;

/**
 * Module for installation wizard application.
 *
 * @author ccordenier
 *
 */
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
    }

}
