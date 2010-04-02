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

package com.wooki.domain.biz;

import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.User;

/**
 * Manager interface to manipulate DAO Interaction.
 */
public interface BookManager
{

    /**
     * Create a new book instance with basic properties initialized.
     * 
     * @param title
     *            The title of the book
     * @return
     */
    @CommitAfter
    Book create(String title);

    /**
     * Remove the book.
     * 
     * @param bookId
     */
    @CommitAfter
    void remove(Long bookId);

    /**
     * Update book.
     * 
     * @param book
     * @return
     */
    @CommitAfter
    Book updateTitle(Book book) throws TitleAlreadyInUseException;

    /**
     * Add an author to a given book, author must exist before calling this method.
     * 
     * @param book
     *            Book must exists in DB before a call to this method.
     * @param title
     *            The tile for the new chapter.
     * @param username
     *            TODO
     * @return TODO
     */
    @CommitAfter
    User addAuthor(Book book, String username) throws UserNotFoundException,
            UserAlreadyOwnerException;

    /**
     * Remove an author from a book.
     */
    @CommitAfter
    void removeAuthor(Book book, Long authorId);

    /**
     * Check if the user is author of a book.
     * 
     * @param book
     * @param username
     * @return
     */
    boolean isAuthor(Book book, String username);

    /**
     * Add a chapter to a given book.
     * 
     * @param book
     *            Book must exists in DB before a call to this method.
     * @param title
     *            The tile for the new chapter.
     * @param username
     *            TODO
     */
    @CommitAfter
    Chapter addChapter(Book book, String title) throws AuthorizationException;

    /**
     * Get the book abstract chapter (first item in the list)
     * 
     * @param bookId
     * @return
     */
    Chapter getBookAbstract(Book book);

    /**
     * Get a book from it short name.
     * 
     * @param title
     * @return
     */
    Book findById(Long id);

    /**
     * Get a book from it short name.
     * 
     * @param title
     * @return
     */
    Book findBookBySlugTitle(String title);

    /**
     * Return the list of existing books.
     * 
     * @return
     */
    List<Book> list();

    /**
     * List the books of an author.
     * 
     * @param userName
     * @return
     */
    List<Book> listByOwner(String userName);

    /**
     * List all the book on which a user has collaborated.
     * 
     * @param userName
     * @return
     */
    List<Book> listByCollaborator(String userName);

    /**
     * Find all the books matching a title.
     * 
     * @param title
     * @return
     */
    public List<Book> listByTitle(String title);

}
