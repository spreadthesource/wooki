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

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.wooki.Draft;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.PublicationXmlException;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.security.WookiSecurityContext;

public class ChapterManagerImpl extends AbstractManager implements ChapterManager
{
    private final ChapterDAO chapterDao;

    private final ActivityDAO activityDao;

    private final CommentDAO commentDao;

    private final PublicationDAO publicationDao;

    private final DOMManager domManager;

    private WookiSecurityContext securityCtx;

    private Logger logger = LoggerFactory.getLogger(ChapterManagerImpl.class);

    private Map<Long, ReentrantLock> locks = CollectionFactory.newConcurrentMap();

    public ChapterManagerImpl(ChapterDAO chapterDAO, ActivityDAO activityDAO,
            CommentDAO commentDAO, PublicationDAO publicationDAO, DOMManager domManager,
            ApplicationContext context)
    {
        this.chapterDao = chapterDAO;
        this.activityDao = activityDAO;
        this.commentDao = commentDAO;
        this.publicationDao = publicationDAO;
        this.domManager = domManager;

        this.securityCtx = context.getBean(WookiSecurityContext.class);
    }

    public Comment addComment(Long publicationId, String content, String domId)
    {

        // Check security
        if (!securityCtx.isLoggedIn()) { throw new AuthorizationException(
                "Only logged user are allowed to add a comments."); }

        User author = securityCtx.getUser();

        if (publicationId == null || content == null) { throw new IllegalArgumentException(
                "Chapter and comment cannot be null for addition."); }

        Publication toUpdate = publicationDao.findById(publicationId);
        Comment comment = new Comment();
        comment.setState(CommentState.OPEN);
        comment.setCreationDate(new Date());
        comment.setDomId(domId);
        comment.setUser(author);
        comment.setContent(content);
        comment.setPublication(toUpdate);
        toUpdate.addComment(comment);
        this.commentDao.create(comment);

        // Log activity
        CommentActivity activity = new CommentActivity();
        activity.setBook(toUpdate.getChapter().getBook());
        activity.setChapter(toUpdate.getChapter());
        activity.setCreationDate(Calendar.getInstance().getTime());
        activity.setComment(comment);
        activity.setType(CommentEventType.POST);
        activity.setUser(author);
        this.activityDao.create(activity);

        return comment;
    }

    public Chapter findById(Long chapterId)
    {
        return this.chapterDao.findById(chapterId);
    }

    public boolean isPublished(Long revision)
    {
        return this.publicationDao.isPublished(revision);
    }

    public Publication getRevision(Long chapterId, String revision)
    {
        assert chapterId != null;
        if (LAST.equalsIgnoreCase(revision) || revision == null) { return publicationDao
                .findLastRevision(chapterId); }
        try
        {
            return this.publicationDao.findRevisionById(chapterId, Long.parseLong(revision));
        }
        catch (NumberFormatException nfEx)
        {
            throw new IllegalArgumentException("Revision number is invalid");
        }
    }

    public String getLastContent(Long chapterId)
    {
        Publication publication = getRevision(chapterId, null);
        if (publication != null) { return publication.getContent(); }
        return null;
    }

    public Publication getLastPublishedPublication(Long chapterId)
    {
        assert chapterId != null;
        return publicationDao.findLastPublishedRevision(chapterId);
    }

    public String getLastPublishedContent(Long chapterId)
    {
        Publication published = getLastPublishedPublication(chapterId);
        if (published != null) { return published.getContent(); }
        return null;
    }

    public Chapter publishChapter(Long chapterId)
    {

        assert chapterId != null;

        Publication published = publicationDao.findLastRevision(chapterId);
        Chapter chapter = this.chapterDao.findById(chapterId);

        if (chapter == null || published == null) { throw new IllegalArgumentException(
                "Cannot find chapter with id " + chapterId); }

        // Check security
        if (!securityCtx.isLoggedIn() || !this.securityCtx.canWrite(chapter.getBook())) { throw new AuthorizationException(
                "Publish action not authorized"); }

        // Adapt content
        String content = null;
        try
        {
            content = domManager.adaptContent(published.getContent(), published.getId());
        }
        catch (PublicationXmlException pxEx)
        {
            logger.error("Unable to publish document", pxEx);
            throw pxEx;
        }

        // Check that the logged user is an author of the book
        User author = securityCtx.getUser();

        // Flag last publication as not published
        Publication lastPublished = publicationDao.findLastPublishedRevision(chapterId);
        if (lastPublished != null)
        {
            lastPublished.setPublished(false);
            publicationDao.update(lastPublished);
        }

        // Publish the last revision
        published.setLastModified(Calendar.getInstance().getTime());
        published.setContent(content);
        published.setPublished(true);
        published.setChapter(chapter);

        publicationDao.update(published);

        ChapterActivity ca = new ChapterActivity();
        ca.setBook(chapter.getBook());
        ca.setChapter(published.getChapter());
        ca.setType(ChapterEventType.PUBLISHED);
        ca.setCreationDate(Calendar.getInstance().getTime());
        ca.setUser(author);
        activityDao.create(ca);

        return chapter;

    }

    public void restoreRevision(Long chapterId, String revision)
    {
        assert chapterId != null;

        Publication toRestore = getRevision(chapterId, revision);
        Chapter chapter = findById(chapterId);
        toRestore.getContent();
        Draft draft = new Draft();
        draft.setData(toRestore.getContent());
        draft.setTimestamp(chapter.getLastModified());
        updateAndPublishContent(chapterId, draft);
    }

    public Chapter update(Chapter chapter)
    {
        assert chapter != null;
        return this.chapterDao.update(chapter);
    }

    public void updateContent(Long chapterId, Draft draft)
    {

        assert chapterId != null;
        assert draft != null;
        assert draft.getData() != null;

        // Update last modified timestamp
        Date lastModified = Calendar.getInstance().getTime();
        ReentrantLock lock = getOrCreateLock(chapterId);

        Publication publication = publicationDao.findLastRevision(chapterId);
        lock.lock();

        Chapter chapter = null;

        try
        {
            chapter = chapterDao.findById(chapterId);
            if (chapter.getLastModified() != null
                    && !chapter.getLastModified().equals(draft.getTimestamp())) { throw new ConcurrentModificationException(
                    "Document has been modified by another user in the meantime."); }
            chapter.setLastModified(lastModified);
            chapterDao.update(chapter);
        }
        finally
        {
            lock.unlock();
        }

        // we check the published flag. If set, then this Publication must
        // be considered as "locked" and we must create a new publication as
        // the new working copy
        if (publication == null || (publication != null && publication.isPublished()))
        {
            publication = new Publication();

            // Security check
            if (!securityCtx.isLoggedIn() || !this.securityCtx.canWrite(chapter.getBook())) { throw new AuthorizationException(
                    "Publish action not authorized"); }
            publication.setChapter(chapter);

            publication.setCreationDate(Calendar.getInstance().getTime());
            publicationDao.create(publication);
        }

        publication.setContent(draft.getData());
        publication.setLastModified(lastModified);
        publicationDao.update(publication);

    }

    public void updateAndPublishContent(Long chapterId, Draft draft)
    {
        updateContent(chapterId, draft);
        publishChapter(chapterId);
    }

    public Chapter remove(Long chapterId)
    {
        assert chapterId != null;

        Chapter toDelete = chapterDao.findById(chapterId);

        if (toDelete == null) { throw new IllegalArgumentException("Cannot find chapter with id "
                + chapterId); }

        if (toDelete != null)
        {
            // The logged user must be allow to write to the book to delete a chapter in it
            if (!securityCtx.isLoggedIn() || !this.securityCtx.canWrite(toDelete.getBook())) { throw new AuthorizationException(
                    "Delete action not authorized"); }

            chapterDao.delete(toDelete);

            ChapterActivity activity = new ChapterActivity();
            activity.setBook(toDelete.getBook());
            activity.setCreationDate(Calendar.getInstance().getTime());
            activity.setChapter(toDelete);
            activity.setUser(this.securityCtx.getUser());
            activity.setType(ChapterEventType.DELETE);
            this.activityDao.create(activity);

            List<Activity> activities = this.activityDao.listAllActivitiesOnChapter(chapterId);
            if (activities != null)
            {
                for (Activity ac : activities)
                {
                    ac.setResourceUnavailable(true);
                    this.activityDao.update(ac);
                }
            }

            // TODO Delete publication entries also ??

        }

        return toDelete;

    }

    public Object[] findPrevious(Long bookId, Long chapterId)
    {
        List<Object[]> result = this.chapterDao.findPrevious(bookId, chapterId);
        if (result != null && result.size() > 0) { return result.get(0); }
        return null;
    }

    public Object[] findNext(Long bookId, Long chapterId)
    {
        List<Object[]> result = this.chapterDao.findNext(bookId, chapterId);
        if (result != null && result.size() > 0) { return result.get(0); }
        return null;
    }

    public List<Chapter> listChapters(Long bookId)
    {
        assert bookId != null;
        return chapterDao.listChapters(bookId);
    }

    public List<Chapter> listChaptersInfo(Long bookId)
    {
        assert bookId != null;
        return chapterDao.listChapterInfo(bookId);
    }

    public List<Publication> listPublicationInfo(Long chapterId)
    {
        assert chapterId != null;
        return publicationDao.listPublication(chapterId);
    }

    private synchronized ReentrantLock getOrCreateLock(Long chapterId)
    {
        assert chapterId != null;

        if (locks.containsKey(chapterId)) { return locks.get(chapterId); }

        ReentrantLock lock = new ReentrantLock();
        locks.put(chapterId, lock);
        return lock;
    }

}
