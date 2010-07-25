package com.wooki.installer.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.spreadthesource.tapestry.installer.InstallerConstants;
import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.wooki.installer.data.DbType;

public class GlobalSettingsTaskImpl implements GlobalSettingsTask
{
    private ApplicationSettings applicationSettings;

    private Map<String, String> settings;
    
    private boolean configured;

    public GlobalSettingsTaskImpl(ApplicationSettings applicationSettings, @Inject @Symbol(InstallerConstants.SILENT_MODE) boolean silent)
    {
        this.applicationSettings = applicationSettings;
        this.settings = new HashMap<String, String>();
        this.configured = silent;

        settings.put(UPLOAD_DIR, System.getProperty("java.io.tmpdir"));
        settings.put(UPLOAD_MAX_FILE_SIZE, "3");

        settings.put(DB_DRIVER, DbType.H2.getDbDriver());
        settings.put(DB_DIALECT, DbType.H2.getDbDialect());
        settings.put(DB_PASSWORD, DbType.H2.getDbPassword());
        settings.put(DB_USERNAME, DbType.H2.getDbUsername());
        settings.put(DB_URL, DbType.H2.getDbUrl());
    }

    public void put(String key, String value)
    {        
        settings.put(key, value);
        
        this.configured = true;
    };

    public String get(String key)
    {
        return settings.get(key);
    };

    public String getStartPage()
    {
        return START_PAGE;
    }

    public Object getStartPageContext()
    {
        return null;
    }

    public boolean isConfigured()
    {
        return containsKeys(
                DB_DIALECT,
                DB_DRIVER,
                DB_PASSWORD,
                DB_URL,
                DB_USERNAME,
                UPLOAD_DIR,
                UPLOAD_MAX_FILE_SIZE) && configured;
    }

    public void rollback()
    {
        settings.clear();
    }

    public void run()
    {
        recordKeys(
                DB_DIALECT,
                DB_DRIVER,
                DB_PASSWORD,
                DB_URL,
                DB_USERNAME,
                UPLOAD_DIR,
                UPLOAD_MAX_FILE_SIZE);
    }

    private boolean containsKeys(String... keys)
    {

        for (String key : keys)
        {
            if (!settings.containsKey(key)) { return false; }
        }

        return true;
    }

    private void recordKeys(String... keys)
    {
        for (String key : keys)
        {
            applicationSettings.put(key, settings.get(key));
        }
    }
}
