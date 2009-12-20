package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.wooki.domain.model.Book;

/**
 * Log activities on book.
 *
 * @author ccordenier
 *
 */
@Entity
@PrimaryKeyJoinColumn(name = "BOOK_ACTIVITY_ID")
public class BookActivity extends Activity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_book", nullable = false)
	private Book book;

	private BookEventType type;
	
	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setType(BookEventType type) {
		this.type = type;
	}

	public BookEventType getType() {
		return type;
	}
	
}
