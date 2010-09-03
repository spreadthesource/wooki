package com.wooki.installer.schema;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.UpdateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(4)
public class BookDescription implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void up()
    {
        // Wooki schema
        helper.add(new UpdateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("Books");
                ctx.addText("abstract");
            }
        });

    }

    public void down()
    {
        // Nothing to do here
    }

}
