//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.pages.book;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This page displays a book with its table of contents.
 */
@Import(library =
{ "context:static/js/jquery-ui-1.7.3.custom.min.js" })
public class Index extends BookBase
{

    @Inject
    private Messages messages;

    @Inject
    private BookManager bookManager;

    @Inject
    private ChapterManager chapterManager;

    @Inject
    private WookiSecurityContext securityCtx;

    @Inject
    private JavaScriptSupport jsSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private Request request;

    @Inject
    private Logger logger;

    @InjectComponent
    private Zone tableOfContents;

    @Property
    private User currentUser;

    @Property
    private int loopIdx;

    @Property
    private List<User> authors;

    @Property
    private List<Chapter> chaptersInfo;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String flashMessage;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String[] errors;

    private Chapter currentChapter;

    /**
     * Will be set if the author tries to add a new chapter
     */
    @Property
    private String chapterName;

    @Property
    private PageLink right;

    private Long firstChapterId;

    private String firstChapterTitle;

    private boolean bookAuthor;

    /**
     * Prepare book display.
     */
    @SetupRender
    public void setupBookDisplay()
    {

        this.authors = this.getBook().getAuthors();
        this.bookAuthor = this.securityCtx.canWrite(this.getBook());

        // List chapter infos
        chaptersInfo = chapterManager.listChaptersInfo(this.getBookId());

        for (Chapter c : chaptersInfo)
        {
            if (isPublished(c.getId()))
            {
                this.firstChapterId = c.getId();
                this.firstChapterTitle = c.getTitle();
                break;
            }
        }

    }

    @SetupRender
    public void setupNav()
    {
        selectMenuItem(0);
        if ((firstChapterId != null) && (firstChapterTitle != null))
        {
            right = new NavLink("chapter/index", "nav-right", firstChapterTitle, getBookId(),
                    firstChapterId);
        }
    }

    @AfterRender
    public void setupChapterSort()
    {
        if (securityCtx.canWrite(getBook()))
        {
            JSONObject params = new JSONObject();
            params.put("url", resources.createEventLink("reorder").toURI());
            jsSupport.addInitializerCall("initAddChapterFocus", new JSONObject());
            jsSupport.addInitializerCall("initSortChapters", params);
            jsSupport.addInitializerCall("initTocRows", new JSONObject());
            jsSupport.addInitializerCall("listenTocUpdate", new JSONObject());
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "addChapterForm")
    public Object addNewChapter()
    {
        try
        {
            bookManager.addChapter(this.getBook(), chapterName);
            flashMessage = messages.format("chapter-add-success", chapterName);
            return tocRefresh();
        }
        catch (Exception ex)
        {
            errors = new String[]
            { messages.get("chapter-add-failure") };
            logger.error("An Error has occured while creating new chapter", ex);
            return this;
        }
    }

    @OnEvent(value = "reorder")
    public void reorder(@RequestParameter(value = "chapterId") Long chapterId,
            @RequestParameter(value = "newPos") int newPos)
    {
        bookManager.updateChapterIndex(getBookId(), chapterId, newPos);
    }

    @OnEvent(value = "publish")
    public Object publish(Long chapterId)
    {
        try
        {
            Chapter toPublish = chapterManager.publishChapter(chapterId);
            flashMessage = messages.format("chapter-publish-success", toPublish.getTitle());
            return tocRefresh();
        }
        catch (Exception ex)
        {
            logger.error("An unexpected exception has occured during publish action", ex);
            errors = new String[]
            { messages.get("chapter-publish-failure") };
            return this;
        }
    }

    @OnEvent(value = "delete")
    public Object delete(Long chapterId)
    {
        try
        {
            Chapter toRemove = chapterManager.remove(chapterId);
            flashMessage = messages.format("chapter-delete-success", toRemove.getTitle());
            return tocRefresh();
        }
        catch (Exception ex)
        {
            logger.error("An unexpected exception has occured during delete action", ex);
            errors = new String[]
            { messages.get("chapter-delete-failure") };
            return this;
        }
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Long retrieveBookId()
    {
        return this.getBookId();
    }

    public boolean isPublished(Long chapterId)
    {
        Publication publication = this.chapterManager.getLastPublishedPublication(chapterId);

        return publication != null;
    }

    public boolean isPublished()
    {
        long chapterId = currentChapter.getId();

        return isPublished(chapterId);
    }

    public boolean isShowWorkingCopyLink()
    {
        long chapterId = currentChapter.getId();
        return hasWorkingCopy(chapterId);
    }

    private final boolean hasWorkingCopy(long chapterId)
    {
        Publication publication = this.chapterManager.getRevision(chapterId, ChapterManager.LAST);
        if (publication != null)
        {
            boolean workingCopy = !publication.isPublished();
            return bookAuthor && workingCopy;
        }
        return false;
    }

    /**
     * Get id to link to chapter display
     * 
     * @return
     */
    public Object[] getChapterCtx()
    {
        return new Object[]
        { this.getBookId(), this.currentChapter.getId() };
    }

    public Object[] getChapterWorkingCopyCtx()
    {
        return new Object[]
        { this.getBookId(), this.currentChapter.getId(), ChapterManager.LAST };
    }

    public Chapter getCurrentChapter()
    {
        return currentChapter;
    }

    public void setCurrentChapter(Chapter currentChapter)
    {
        this.currentChapter = currentChapter;
    }

    /**
     * If request is XHR then return the TOC block, otherwise refresh the whole page.
     * 
     * @return
     */
    private Object tocRefresh()
    {
        if (request.isXHR())
        {
            setupBookDisplay();
            return tableOfContents.getBody();
        }
        return null;
    }

}
