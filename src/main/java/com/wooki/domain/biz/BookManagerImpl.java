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

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.TitleAlreadyInUseException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.services.db.QueryFilterService;
import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.utils.SlugBuilder;

/**
 * Global wooki application business manager.
 */
public class BookManagerImpl extends AbstractManager implements BookManager
{
    private final BookDAO bookDao;

    private final UserDAO userDao;

    private final ActivityDAO activityDao;

    private final ChapterDAO chapterDao;

    private final QueryFilterService filterService;

    private final SecurityManager securityManager;

    private final WookiSecurityContext securityCtx;

    public BookManagerImpl(BookDAO bookDAO, UserDAO userDAO, ActivityDAO activityDAO,
            ChapterDAO chapterDAO, ApplicationContext context, QueryFilterService filterService)
    {
        this.bookDao = bookDAO;
        this.userDao = userDAO;
        this.activityDao = activityDAO;
        this.chapterDao = chapterDAO;
        this.filterService = filterService;

        this.securityCtx = context.getBean(WookiSecurityContext.class);
        this.securityManager = context.getBean(SecurityManager.class);
    }

    public User addAuthor(Book book, String username) throws UserNotFoundException,
            UserAlreadyOwnerException
    {

        assert book != null;
        assert username != null;

        // Security checks
        if (!this.securityCtx.isLoggedIn() || !this.securityCtx.isOwner(book)) { throw new AuthorizationException(
                "Action not authorized"); }

        User toAdd = userDao.findByUsername(username);

        if (toAdd == null) { throw new UserNotFoundException(username + " does not exist."); }

        if (bookDao.isAuthor(book.getId(), username)) { throw new UserAlreadyOwnerException(
                username + "is already an author the book"); }

        book.addUser(toAdd);
        bookDao.update(book);
        this.securityManager.setCollaboratorPermission(book, toAdd);

        return toAdd;
    }

    public void remove(Long bookId)
    {
        assert bookId != null;

        Book toRemove = this.bookDao.findById(bookId);

        if (toRemove != null)
        {
            // Security check
            if (!this.securityCtx.isLoggedIn() || !this.securityCtx.canDelete(toRemove)) { throw new AuthorizationException(
                    "Operation not allowed"); }

            this.bookDao.delete(toRemove);

            BookActivity ba = new BookActivity();
            ba.setCreationDate(Calendar.getInstance().getTime());
            ba.setType(BookEventType.DELETE);
            ba.setUser(this.securityCtx.getUser());
            ba.setBook(toRemove);
            ba.setResourceUnavailable(true);
            this.activityDao.create(ba);

            // Update activity states
            List<Activity> activities = this.activityDao.listAllActivitiesOnBook(bookId);
            if (activities != null)
            {
                for (Activity a : activities)
                {
                    a.setResourceUnavailable(true);
                    this.activityDao.update(a);
                }
            }

            // Remove also chapters
            if (toRemove.getChapters() != null)
            {
                for (Chapter chapter : toRemove.getChapters())
                {
                    this.chapterDao.delete(chapter);
                }
            }

        }

    }

    public void removeAuthor(Book book, Long authorId)
    {

        assert book != null;
        assert authorId != null;

        if (!this.securityCtx.isLoggedIn() || !this.securityCtx.isOwner(book)) { throw new AuthorizationException(
                "Action not authorized"); }

        User user = userDao.findById(authorId);
        user.getBooks().remove(book);
        book.getAuthors().remove(user);
        bookDao.update(book);
        
        securityManager.removeCollaboratorPermission(book, user);
    }

    public Book updateTitle(Book book) throws TitleAlreadyInUseException
    {

        assert book != null;

        if (!this.securityCtx.isLoggedIn() || !this.securityCtx.canWrite(book)) { throw new AuthorizationException(
                "Action not authorized"); }

        // Create slug title
        String slug = SlugBuilder.buildSlug(book.getTitle());

        // If book has changed of title
        if (book.getSlugTitle() != null && !book.getSlugTitle().equalsIgnoreCase(slug))
        {
            Book result = bookDao.findBookBySlugTitle(slug);
            if (result != null) { throw new TitleAlreadyInUseException(); }
        }

        if (!book.getSlugTitle().equals(slug))
        {
            book.setSlugTitle(slug);
        }
        return bookDao.update(book);
    }

    public boolean isAuthor(Book book, String username)
    {

        assert book != null;
        assert username != null;

        return bookDao.isAuthor(book.getId(), username);
    }

    public Chapter addChapter(Book book, String title) throws AuthorizationException
    {

        assert book != null;
        assert title != null;

        if (!securityCtx.canWrite(book)) { throw new AuthorizationException(
                "Current user is not an author of " + book.getTitle()); }

        User author = securityCtx.getUser();

        // Create the new Chapter
        Chapter chapter = new Chapter();
        chapter.setTitle(title);
        chapter.setSlugTitle(SlugBuilder.buildSlug(title));
        Date creationDate = Calendar.getInstance().getTime();
        chapter.setCreationDate(creationDate);
        chapter.setLastModified(creationDate);

        // Get managed entity to update
        book = bookDao.findById(book.getId());
        book.addChapter(chapter);
        chapter.setBook(book);
        this.chapterDao.create(chapter);

        // Add activity event
        ChapterActivity activity = new ChapterActivity();
        activity.setBook(book);
        activity.setUser(author);
        activity.setCreationDate(creationDate);
        activity.setType(ChapterEventType.CREATE);
        activity.setChapter(chapter);
        activityDao.create(activity);

        return chapter;
    }

    public Book create(String title)
    {

        assert title != null;

        if (!this.securityCtx.isLoggedIn()) { throw new AuthorizationException(
                "Only logged user can create books."); }

        User author = securityCtx.getUser();

        Book book = new Book();

        // Set basic properties
        book.setTitle(title);
        book.setSlugTitle(SlugBuilder.buildSlug(title));
        Date creationDate = Calendar.getInstance().getTime();
        book.setCreationDate(creationDate);
        book.setLastModified(creationDate);

        // Add abstract
        Chapter bookAbstract = new Chapter();
        bookAbstract.setCreationDate(creationDate);
        bookAbstract.setTitle("Abstract");
        bookAbstract.setSlugTitle("Abstract");
        book.addChapter(bookAbstract);
        book.setOwner(author);
        book.addUser(author);
        bookAbstract.setBook(book);

        bookDao.create(book);

        // Add activity event
        BookActivity activity = new BookActivity();
        activity.setUser(author);
        activity.setBook(book);
        activity.setCreationDate(creationDate);
        activity.setType(BookEventType.CREATE);
        activityDao.create(activity);

        // Set permissions
        this.securityManager.setOwnerPermission(book);

        return book;
    }

    public Chapter getBookAbstract(Book book)
    {
        if (book == null) { throw new IllegalArgumentException("Book parameter cannot be null"); }
        Book upToDate = bookDao.findById(book.getId());
        if (book != null) { return upToDate.getChapters().get(0); }
        return null;
    }

    public Book findBookBySlugTitle(String title)
    {
        return bookDao.findBookBySlugTitle(title);
    }

    public Book findById(Long id)
    {
        return bookDao.findById(id);
    }

    public List<Book> list()
    {
        return bookDao.list(filterService.present());
    }

    public List<Book> listByTitle(String title)
    {
        return bookDao.listByTitle(title);
    }

    public List<Book> listByOwner(String userName)
    {
        User author = userDao.findByUsername(userName);
        if (author != null) { return bookDao.listByOwner(author.getId()); }
        return null;
    }

    public List<Book> listByCollaborator(String userName)
    {
        User author = userDao.findByUsername(userName);
        if (author != null) { return bookDao.listByCollaborator(author.getId()); }
        return null;
    }

}
