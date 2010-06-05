package com.wooki.app0.services;

import com.spreadthesource.tapestry.installer.services.ApplicationSettings;

/**
 * Use this module to provide mock services that will be used only for unit test purpose.
 * 
 * @author ccordenier
 */
public class UnitTestModule
{

    public ApplicationSettings decorateApplicationSettings(ApplicationSettings delegate)
    {
        return new UnitTestApplicationSettings();
    }

}
