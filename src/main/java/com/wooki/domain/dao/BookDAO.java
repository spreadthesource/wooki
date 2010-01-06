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

package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Book;

/**
 * DAO in charge of the Book
 * 
 * @author ccordenier
 * 
 */
public interface BookDAO extends GenericDAO<Book, Long> {

	/**
	 * 
	 * @param title
	 * @return
	 */
	Book findBookBySlugTitle(String title);

	/**
	 * List book with similar title.
	 * 
	 * @param title
	 * @return
	 */
	List<Book> listByTitle(String title);

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	List<Book> listByAuthor(Long id);

	/**
	 * Verify if an author owns a book. An author has lesser privileges than the
	 * owner.
	 * 
	 * @return
	 */
	boolean isAuthor(Long bookId, String username);

	/**
	 * Check if the user is owner of the book, this will allow him to modify
	 * book settings and list of authors.
	 * 
	 * @param bookId
	 * @param username
	 * @return
	 */
	boolean isOwner(Long bookId, String username);
}
