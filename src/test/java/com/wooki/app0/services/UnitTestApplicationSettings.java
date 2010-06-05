package com.wooki.app0.services;

import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.spreadthesource.tapestry.installer.services.ApplicationSettings;

public class UnitTestApplicationSettings implements ApplicationSettings
{

    private Map<String, String> settings = CollectionFactory.newCaseInsensitiveMap();

    public UnitTestApplicationSettings()
    {
        settings.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        settings.put("hibernate.connection.url", "jdbc:hsqldb:mem:wookidbunittest");
        settings.put("hibernate.connection.username", "sa");
        settings.put("hibernate.connection.password", "");
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    }

    public boolean alreadyInstalled()
    {
        return true;
    }

    public boolean containsKey(String key)
    {
        return settings.containsKey(key);
    }

    public String get(String key)
    {
        return settings.get(key);
    }

    public void put(String key, String value)
    {
        settings.put(key, value);
    }

    public String valueForSymbol(String symbolName)
    {
        return get(symbolName);
    }

}
