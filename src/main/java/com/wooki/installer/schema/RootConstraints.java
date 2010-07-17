package com.wooki.installer.schema;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.migrations.Constraint;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(3)
public class RootConstraints implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void up()
    {
        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("Books");
                ctx.setForeignKey("books_owner_constraint", "Users", "owner_id", "user_id");
            }
        });

        helper.add(new CreateConstraint()
        {

            public void run(Constraint ctx)
            {
                ctx.setTableName("Chapters");
                ctx.setForeignKey("chapters_book_constraint", "Books", "book_id", "book_id");
            }
        });

        helper.add(new CreateConstraint()
        {

            public void run(Constraint ctx)
            {
                ctx.setTableName("Comments");
                ctx.setForeignKey(
                        "comments_publication_constraint",
                        "Publications",
                        "publication_id",
                        "publication_id");

            }
        });

        helper.add(new CreateConstraint()
        {

            public void run(Constraint ctx)
            {
                ctx.setTableName("Comments");
                ctx.setForeignKey(
                        "coments_commentlabel_constraint",
                        "CommentsLabels",
                        "comment_label_id",
                        "comment_label_id");

            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("Comments");
                ctx.setForeignKey("comments_user_constraint", "Users", "user_id", "user_id");

            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("Publications");
                ctx.setForeignKey("publications_chapter_constraint", "Chapters", "chapter_id", "chapter_id");
            }
        });

        helper.add(new CreateConstraint()
        {

            public void run(Constraint ctx)
            {
                ctx.setTableName("AbstractBooksActivities");
                ctx.setForeignKey("aba_book_constraint", "Books", "book_id", "book_id");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("AbstractChaptersActivities");
                ctx.setForeignKey("aca_chapter_constraint", "Chapters", "chapter_id", "chapter_id");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("Activities");
                ctx.setForeignKey("a_user_constraint", "Users", "user_id", "user_id");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("CommentsActivities");
                ctx.setForeignKey("ca_comment_constraint", "Comments", "comment_id", "comment_id");
            }
        });
    }

    public void down()
    {
        helper.add(new Drop()
        {
            
            public void run(DropContext ctx)
            {
                ctx.dropForeignKey("Books", "books_owner_constraint");
                ctx.dropForeignKey("Chapters", "chapters_book_constraint");
                ctx.dropForeignKey("Comments", "comments_publication_constraint");
                ctx.dropForeignKey("Comments", "coments_commentlabel_constraint");
                ctx.dropForeignKey("Comments", "comments_user_constraint");
                ctx.dropForeignKey("Publications", "publications_chapter_constraint");
                ctx.dropForeignKey("AbstractBooksActivities", "aba_book_constraint");
                ctx.dropForeignKey("AbstractChaptersActivities", "aca_chapter_constraint");
                ctx.dropForeignKey("Activities", "a_user_constraint");
                ctx.dropForeignKey("CommentsActivities", "ca_comment_constraint");
            }
        });
    }

}
