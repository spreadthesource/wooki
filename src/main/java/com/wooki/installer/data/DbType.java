package com.wooki.installer.data;

public enum DbType
{
    H2("org.h2.Driver", "org.hibernate.dialect.H2Dialect", "sa", null, "jdbc:h2:target/wookidb"), 
    HSQLDB("org.hsqldb.jdbcDriver", "org.hibernate.dialect.HSQLDialect", "sa", null, "jdbc:hsqldb:mem:wookidb");
    
    private String dbDriver;

    private String dbDialect;

    private String dbUsername;
    
    private String dbPassword;

    private String dbUrl;


    DbType(String dbDriver, String dbDialect, String dbUsername, String dbPassword, String dbUrl)
    {
        this.dbDriver = dbDriver;
        this.dbDialect = dbDialect;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbUrl = dbUrl;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver)
    {
        this.dbDriver = dbDriver;
    }

    public String getDbDialect()
    {
        return dbDialect;
    }

    public void setDbDialect(String dbDialect)
    {
        this.dbDialect = dbDialect;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

    public String getDbUrl()
    {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }
}
