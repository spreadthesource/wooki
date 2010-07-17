package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.wooki.domain.model.Book;

/**
 * Defines entities that are related to a book.
 * 
 * @author ccordenier
 */
@Entity
@Table(name = "AbstractBooksActivities")
@PrimaryKeyJoinColumn(name = "abstract_book_activity_id")
public abstract class AbstractBookActivity extends Activity
{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

}
