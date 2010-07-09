package com.wooki.installer.schema;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.command.JoinTable;
import com.spreadthesource.tapestry.dbmigration.migrations.Constraint;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;
import com.spreadthesource.tapestry.dbmigration.migrations.impl.CreateTableImpl;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(1)
public class Root implements Migration
{

    @Inject
    private MigrationHelper helper;

    static class CreateWookiTable extends CreateTableImpl
    {
        public CreateWookiTable(String name)
        {
            super(name);
        }

        @Override
        public void run(Table ctx)
        {
            super.run(ctx);
            ctx.addTimestamp("creationDate").setNotNull(true);
            ctx.addTimestamp("deletionDate");
            ctx.addTimestamp("lastModified");
        }

    }

    public void up()
    {

        // Wooki schema
        helper.add(new CreateWookiTable("Authority")
        {

            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addString("authority");
            }

        });

        helper.add(new CreateWookiTable("User")
        {
            @Override
            public void run(Table ctx)
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
            public void run(Table ctx)
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
            public void run(Table ctx)
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
            public void run(Table ctx)
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
            public void run(Table ctx)
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
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addString("label");
            }
        });

        helper.add(new CreateWookiTable("Activity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addBoolean("resourceUnavailable");
                ctx.addLong("user_id");
            }
        });

        helper.add(new CreateTableImpl("AbstractBookActivity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("book_id");
            }
        });

        helper.add(new CreateTableImpl("AccountActivity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("AbstractChapterActivity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("chapter_id");
            }
        });

        helper.add(new CreateTableImpl("BookActivity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("ChapterActivity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addInteger("type");
            }
        });

        helper.add(new CreateTableImpl("CommentActivity")
        {
            @Override
            public void run(Table ctx)
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

        // ACL schema
        helper.add(new CreateTableImpl("acl_sid")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("id").setPrimary(true).setUnique(true).setIdentityGenerator("identity");
                ctx.addBoolean("principal").setNotNull(true);
                ctx.addString("sid").setNotNull(true);
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setName("acl_sid");
                ctx.setUnique("unique_uk_1", "sid", "principal");
            }
        });

        helper.add(new CreateTableImpl("acl_class")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("id").setPrimary(true).setUnique(true).setIdentityGenerator("identity");
                ctx.addString("class").setNotNull(true).setUnique(true);
            }
        });

        helper.add(new CreateTableImpl("acl_object_identity")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("id").setPrimary(true).setUnique(true).setIdentityGenerator("identity");
                ctx.addLong("object_id_class").setNotNull(true);
                ctx.addLong("object_id_identity").setNotNull(true);
                ctx.addLong("parent_object");
                ctx.addLong("owner_sid");
                ctx.addBoolean("entries_inheriting");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setName("acl_object_identity");
                ctx.setUnique("unique_uk_3", "object_id_class", "object_id_identity");
                ctx.setForeignKey("foreign_fk_1", "acl_object_identity", new String[]
                { "parent_object" }, new String[]
                { "id" });

                ctx.setForeignKey("foreign_fk_2", "acl_class", new String[]
                { "object_id_class" }, new String[]
                { "id" });

                ctx.setForeignKey("foreign_fk_3", "acl_sid", new String[]
                { "owner_sid" }, new String[]
                { "id" });
            }
        });

        helper.add(new CreateTableImpl("acl_entry")
        {
            @Override
            public void run(Table ctx)
            {
                super.run(ctx);
                ctx.addLong("id").setPrimary(true).setUnique(true).setIdentityGenerator("identity");
                ctx.addLong("acl_object_identity").setNotNull(true);
                ctx.addInteger("ace_order").setNotNull(true);
                ctx.addLong("sid").setNotNull(true);
                ctx.addInteger("mask").setNotNull(true);
                ctx.addBoolean("granting");
                ctx.addBoolean("audit_success");
                ctx.addBoolean("audit_failure");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setName("acl_entry");
                ctx.setUnique("unique_uk_4", "acl_object_identity", "ace_order");

                ctx.setForeignKey("foreign_fk_4", "acl_object_identity", new String[]
                { "acl_object_identity" }, new String[]
                { "id" });

                ctx.setForeignKey("foreign_fk_5", "acl_sid", new String[]
                { "sid" }, new String[]
                { "id" });
            }
        });

    }

    public void down()
    {
        helper.add(new Drop()
        {
            public void run(DropContext ctx)
            {
                // drop constraints
                ctx.dropForeignKey("acl_object_identity", "foreign_fk_1");
                ctx.dropForeignKey("acl_object_identity", "foreign_fk_2");
                ctx.dropForeignKey("acl_object_identity", "foreign_fk_3");
                ctx.dropForeignKey("acl_entry", "foreign_fk_4");
                ctx.dropForeignKey("acl_entry", "foreign_fk_5");

                // Drop tables
                ctx.dropTable("acl_sid");
                ctx.dropTable("acl_class");
                ctx.dropTable("acl_object_identity");
                ctx.dropTable("acl_entry");
            }
        });
    }

}
