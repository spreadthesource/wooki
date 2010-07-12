package com.wooki.installer.schema;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.Sql;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.SqlQuery;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(2)
public class RootConstraint implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void down()
    {

    }

    public void up()
    {
        helper.add(new Sql()
        {
            public void run(SqlQuery ctx)
            {
                ctx.addSql("insert into Authority(authority_id, authority, creationDate) values (1, 'ROLE_ADMIN', '2010-07-12 08:48:47')");
                ctx.addSql("insert into Authority(authority_id, authority, creationDate) values (2, 'ROLE_AUTHOR', '2010-07-12 08:48:47')");
                
                ctx.addSql(String.format("insert into User(user_id, fullname, email, username, password, creationDate) values (1, 'root', 'root@yourdomain.com', 'root', 'f26c00488540ea41f52d2e216da55ecef8d063bb', '2010-07-12 08:48:47')"));
                
                ctx.addSql("insert into UserAuthority(user_id, authority_id) values(1, 1)");
                ctx.addSql("insert into AuthorityUser(user_id, authority_id) values(1, 1)");
            }
        });
    }

}
