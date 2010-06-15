package com.wooki.installer.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.spreadthesource.tapestry.installer.services.Restart;
import com.wooki.installer.data.DbType;

public class Index
{
    @Inject
    private ApplicationSettings settings;

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
    @Validate("required")
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
    public void initValues() {
        if (dbType == null) {
            dbType = DbType.H2;
        }
        
        uploadMaxFileSize = 3;
        
        initDbValues(dbType);
    }

    @OnEvent(value = EventConstants.ACTION)
    public Object addConfiguration()
    {
        settings.put("hello", "hi");
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
        settings.put("uploadDir", uploadDir);
        settings.put("uploadMaxFileSize", uploadMaxFileSize.toString());
        
        settings.put("dbDialect", dbDialect);
        settings.put("dbDriver", dbDriver);
        settings.put("dbPassword", dbPassword);
        settings.put("dbUrl", dbUrl);
        settings.put("dbUsername", dbUsername);
        
        return new Restart();
    }

    public SelectModel getDbSelectionModel()
    {
        return new EnumSelectModel(DbType.class, messages);
    }

    public ValueEncoder<DbType> getDbSelectionEncoder()
    {
        return new EnumValueEncoder<DbType>(DbType.class);
    }
    
    private void initDbValues(DbType dbType) {
        switch (dbType)
        {
            case H2:
                dbDriver = "org.h2.Driver";
                dbDialect = "org.hibernate.dialect.H2Dialect";
                dbUsername = "sa";
                dbUrl = "jdbc:h2:target/wookidb";
                break;

            case HSQLDB:
                dbDriver = "org.hsqldb.jdbcDriver";
                dbDialect = "org.hibernate.dialect.HSQLDialect";
                dbUsername = "sa";
                dbUrl = "jdbc:hsqldb:mem:wookidb";
                break;

            default:
                break;
        }
    }
}
