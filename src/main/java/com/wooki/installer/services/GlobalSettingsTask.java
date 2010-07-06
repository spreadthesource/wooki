package com.wooki.installer.services;

import com.spreadthesource.tapestry.installer.services.ConfigurationTask;

public interface GlobalSettingsTask extends ConfigurationTask
{
    public final String UPLOAD_DIR = "uploadDir";

    public final String UPLOAD_MAX_FILE_SIZE = "uploadMaxFileSize";
    
    public final String DB_USERNAME = "hibernate.connection.username";

    public final String DB_PASSWORD = "hibernate.connection.password";

    public final String DB_URL = "hibernate.connection.url";

    public final String DB_DRIVER = "hibernate.connection.driver_class";

    public final String DB_DIALECT = "hibernate.dialect";

    public final String START_PAGE = "index";

    public void put(String key, String value);

    public String get(String key);
}
