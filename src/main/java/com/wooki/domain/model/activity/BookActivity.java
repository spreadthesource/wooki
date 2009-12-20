//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
