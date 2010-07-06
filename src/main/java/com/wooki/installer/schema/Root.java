package com.wooki.installer.schema;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTable;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.CreateTableImpl;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(1)
public class Root implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void down()
    {

    }

    public void up()
    {

        helper.add(new CreateWookiTable("Authority")
        {

            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("authority");
            }

        });

        helper.add(new CreateWookiTable("User")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("email").setNotNull(true);
                ctx.addString("username").setUnique(true).setNotNull(true);
                ctx.addString("password").setNotNull(true);
                ctx.addString("fullname").setNotNull(true);
            }
        });

        helper.add(new CreateWookiTable("Book")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("slugTitle").setUnique(true);
                ctx.addString("title");
                ctx.addLong("owner_id");
            }
        });

        helper.add(new CreateWookiTable("Chapter")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("title");
                ctx.addString("slugTitle");
                ctx.addLong("book_id");
                ctx.addInteger("chapter_position");
            }
        });

        helper.add(new CreateWookiTable("Publication")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addBoolean("published");
                ctx.addText("content");
                ctx.addLong("chapter_id");
            }
        });

        helper.add(new CreateWookiTable("Comment")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("title");
                ctx.addString("state");
                ctx.addString("domId");
                ctx.addText("content");
                ctx.addLong("publication_id");
                ctx.addLong("comment_label_id");
                ctx.addLong("user_id");
            }
        });

        helper.add(new CreateWookiTable("CommentLabel")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addString("label");
            }
        });

        helper.add(new CreateWookiTable("Activity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addBoolean("resourceUnavailable");
                ctx.addLong("user_id");
            }
        });

        helper.add(new CreateTableImpl("AbstractBookActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addLong("book_id");
            }
        });

        helper.add(new CreateTableImpl("AccountActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("AbstractChapterActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addLong("chapter_id");
            }
        });

        helper.add(new CreateTableImpl("BookActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("ChapterActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("CommentActivity")
        {
            @Override
            public void run(CreateTableContext ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
                ctx.addLong("comment_id");
            }
        });

        helper.add(new JoinTable()
        {
            public void run(JoinTableContext ctx)
            {
                ctx.join("Authority", "User");
                ctx.join("User", "Authority");
                ctx.join("Book", "User");
                ctx.join("BookAuthor", "Book", "User");
            }
        });

    }

    static class CreateWookiTable extends CreateTableImpl
    {
        public CreateWookiTable(String name)
        {
            super(name);
        }

        @Override
        public void run(CreateTableContext ctx)
        {
            super.run(ctx);
            ctx.addTimestamp("creationDate").setNotNull(true);
            ctx.addTimestamp("deletionDate");
            ctx.addTimestamp("lastModified");
        }

    }

}
