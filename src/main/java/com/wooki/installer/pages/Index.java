package com.wooki.installer.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import com.spreadthesource.tapestry.installer.services.ConfigurationManager;
import com.spreadthesource.tapestry.installer.services.ConfigurationTask;
import com.spreadthesource.tapestry.installer.services.Restart;
import com.wooki.installer.data.DbType;
import com.wooki.installer.services.GlobalSettingsTask;

public class Index
{
    @Inject
    private ConfigurationManager manager;

    @Inject
    private GlobalSettingsTask settings;

    @Inject
    private Messages messages;

    @Inject
    @Property
    private Block dbDetails;

    @Property
    @Validate("required")
    private String uploadDir;

    @Property
    @Validate("required")
    private Integer uploadMaxFileSize;

    @Property
    @Validate("required")
    private DbType dbType;

    @Property
    @Validate("required")
    private String dbUsername;

    @Property
    private String dbPassword;

    @Property
    @Validate("required")
    private String dbUrl;

    @Property
    @Validate("required")
    private String dbDriver;

    @Property
    @Validate("required")
    private String dbDialect;

    @OnEvent(value = EventConstants.ACTIVATE)
    public void initValues()
    {
        if (dbType == null)
        {
            dbType = DbType.H2;
        }

        uploadMaxFileSize = 3;

        initDbValues(dbType);
    }

    @OnEvent(value = EventConstants.ACTION)
    public Object addConfiguration()
    {
        return new Restart();
    }

    @OnEvent(value = EventConstants.VALUE_CHANGED)
    public Object dbSelectionChanged(final DbType selectedDbType)
    {
        initDbValues(selectedDbType);

        return dbDetails;
    }

    @OnEvent(value = EventConstants.SUBMIT)
    public Object submitConfiguration()
    {
        settings.put(GlobalSettingsTask.UPLOAD_DIR, uploadDir);
        settings.put(GlobalSettingsTask.UPLOAD_MAX_FILE_SIZE, uploadMaxFileSize.toString());

        settings.put(GlobalSettingsTask.DB_DIALECT, dbDialect);
        settings.put(GlobalSettingsTask.DB_DRIVER, dbDriver);
        settings.put(GlobalSettingsTask.DB_PASSWORD, dbPassword);
        settings.put(GlobalSettingsTask.DB_URL, dbUrl);
        settings.put(GlobalSettingsTask.DB_USERNAME, dbUsername);

        manager.configure();
        ConfigurationTask task = manager.getCurrentTask();

        return task.getStartPage();
    }

    public SelectModel getDbSelectionModel()
    {
        return new EnumSelectModel(DbType.class, messages);
    }

    public ValueEncoder<DbType> getDbSelectionEncoder()
    {
        return new EnumValueEncoder<DbType>(DbType.class);
    }

    private void initDbValues(DbType dbType)
    {
        dbDialect = dbType.getDbDialect();
        dbUrl = dbType.getDbUrl();
        dbDriver = dbType.getDbDriver();
        dbUsername = dbType.getDbUsername();
        dbPassword = dbType.getDbPassword();
    }
}
