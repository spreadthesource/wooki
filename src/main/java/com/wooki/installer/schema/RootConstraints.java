package com.wooki.installer.schema;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;

@Version(2)
public class RootConstraints implements Migration
{

    public void down()
    {

    }

    public void up()
    {
        /** Table book = prepare("Book");
        Constraint c = book.addForeignKey("fk_book_owner_id", "User");
        c.addConstraint("owner_id", "user_id");

        Table chapter = prepare("Chapter");
        c = chapter.addForeignKey("fk_chapter_book", "Book");
        c.addConstraint("book_id", "book_id");

        Table comment = prepare("Comment");
        c = comment.addForeignKey("fk_publication_id", "Publication");
        c.addConstraint("publication_id", "publication_id");

        createTable(book);
        createTable(chapter);
        createTable(comment); */
    }

}
