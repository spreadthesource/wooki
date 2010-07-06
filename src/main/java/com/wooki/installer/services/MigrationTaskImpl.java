package com.wooki.installer.services;

import com.spreadthesource.tapestry.dbmigration.services.MigrationManager;

public class MigrationTaskImpl implements MigrationTask
{
    private MigrationManager manager;

    public MigrationTaskImpl(MigrationManager manager)
    {
        this.manager = manager;
    }

    public String getStartPage()
    {
        return null;
    }

    public Object getStartPageContext()
    {
        return null;
    }

    public boolean isConfigured()
    {
        return true;
    }

    public void rollback()
    {
        manager.reset();
    }

    public void run()
    {
        manager.migrate();
    }

}
