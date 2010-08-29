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

package com.wooki.pages.chapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.apache.tapestry5.util.TextStreamResponse;

import com.ibm.icu.util.Calendar;
import com.wooki.Draft;
import com.wooki.Drafts;
import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.exception.PublicationXmlException;
import com.wooki.domain.model.Chapter;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;
import com.wooki.links.impl.ViewLink;

/**
 * This page is used to update/publish a chapter of a given book.
 * 
 * @author ccordenier
 */
@Import(library =
{ "context:/static/js/jquery.notifyBar.js", "context:/static/js/notifybar.js" }, stylesheet =
{ "context:/static/css/jquery.notifyBar.css" })
public class Edit extends BookBase
{

    @Inject
    private ChapterManager chapterManager;

    @Inject
    private Block titleBlock;

    @Inject
    private Messages messages;

    @Inject
    private JavaScriptSupport jsSupport;

    @Inject
    private Request request;

    @InjectComponent
    private Form editChapterForm;

    @InjectPage
    private Index index;

    @Inject
    @Symbol(UploadSymbols.FILESIZE_MAX)
    private long maxFileSize;

    @SessionState
    private Drafts drafts;

    private Long chapterId;

    @Property
    private Chapter chapter;

    @Property
    @Validate("required")
    private String data;

    @Property
    private Long previous;

    @Property
    private String previousTitle;

    @Property
    private Long next;

    @Property
    private String nextTitle;

    @Property
    private boolean abstractChapter;

    @Property
    private PageLink left;

    @Property
    private PageLink right;

    @Property
    private PageLink center;

    @Property
    private Draft draft;

    private DateFormat format = new SimpleDateFormat("hh:mm");

    private boolean publish;

    private boolean cancel;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object onActivate(Long bookId, Long chapterId)
    {

        this.chapterId = chapterId;
        chapter = chapterManager.findById(chapterId);

        if (chapter == null) { return redirectToBookIndex(); }

        draft = drafts.getOrCreate(chapter);

        return null;
    }

    @SetupRender
    public void prepareFormData()
    {
        // If currently edited then get it from user session, otherwise took content from db
        if (draft.getData() != null)
        {
            data = draft.getData();
        }
        else
        {
            data = chapterManager.getLastContent(chapterId);
        }

        // Check if we are editing the abstract chapter
        if (getBook().getChapters() != null && getBook().getChapters().size() > 0
                && getBook().getChapters().get(0).getId().equals(chapterId))
        {
            abstractChapter = true;
        }

        // Prepare previous and next links
        Object[] data = chapterManager.findPrevious(getBookId(), chapterId);
        if (data != null && data.length == 2)
        {
            previous = (Long) data[0];
            previousTitle = (String) data[1];
        }

        data = chapterManager.findNext(getBookId(), chapterId);
        if (data != null && data.length == 2)
        {
            next = (Long) data[0];
            nextTitle = (String) data[1];
        }
    }

    @SetupRender
    public void setupNav()
    {
        if ((previous != null) && (previousTitle != null))
        {
            left = new NavLink("chapter/index", "nav-left", "cancel-edition", previousTitle,
                    getBookId(), previous);
        }
        else
        {
            left = new ViewLink("book/index", "toc", getBookId());
        }

        if ((next != null) && (nextTitle != null))
        {
            right = new NavLink("chapter/index", "nav-right", "cancel-edition", nextTitle,
                    getBookId(), next);
        }
        center = new NavLink("book/index", "book-root", "cancel-edition", getBook().getTitle(),
                getBookId());

    }

    @AfterRender
    public void setupJs()
    {
        this.jsSupport.addInitializerCall("initUpdateTitleFocus", new JSONObject());
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "updateTitle")
    public Object updateTitle()
    {
        chapterManager.update(chapter);
        return titleBlock;
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Object[] retrieveIds()
    {
        return new Object[]
        { getBookId(), chapterId };
    }

    /**
     * Used to check which submit button has been clicked
     */
    public void onPublish()
    {
        publish = true;
    }

    /**
     * Used to check which submit button has been clicked
     */
    public void onUpdate()
    {
        publish = false;
    }

    /**
     * Update content and publish if requested.
     * 
     * @return The book index page
     */
    @OnEvent(value = EventConstants.SUCCESS, component = "editChapterForm")
    public Object updateChapter()
    {

        draft.setData(data);

        // If autosave then save in session and return
        if (request.isXHR())
        {
            JSONObject result = new JSONObject();
            result.put("message", messages.format("autosave-success", format.format(Calendar
                    .getInstance().getTime())));
            return result;
        }

        try
        {
            if (!cancel)
            {
                chapterManager.updateContent(chapterId, draft);
                if (publish)
                {
                    chapterManager.publishChapter(chapterId);
                }
            }

            index.setBookId(getBookId());

            if (publish)
            {
                index.setupChapter(getBookId(), chapterId);
            }
            else
            {
                index.setupChapter(getBookId(), chapterId, ChapterManager.LAST);
                index.setRevision(ChapterManager.LAST);
            }

            // Clean session
            drafts.remove(chapter);

            return index;

        }
        catch (PublicationXmlException pxEx)
        {
            editChapterForm.recordError(messages.get("publication-exception"));
            return this;
        }
        catch (ConcurrentModificationException cmEx)
        {
            // Update the draft and inform the user
            draft.setTimestamp(chapter.getLastModified());
            editChapterForm.recordError(messages.get("concurrent-modification-exception"));
            return this;
        }

    }

    public Object[] getCancelCtx()
    {
        return new Object[]
        { getBookId(), chapterId };
    }

    public Long getChapterId()
    {
        return chapterId;
    }

    public void setChapterId(Long chapterId)
    {
        this.chapterId = chapterId;
    }

    /**
     * Handle upload exception
     * 
     * @param ex
     * @return
     */
    public Object onUploadException(FileUploadException ex)
    {
        JSONObject result = new JSONObject();
        JSONArray message = new JSONArray();
        result.put("error", true);
        message.put(messages.format("upload-exception", maxFileSize / 1024));
        result.put("messages", message);
        return new TextStreamResponse("text/html", result.toString());
    }

}
