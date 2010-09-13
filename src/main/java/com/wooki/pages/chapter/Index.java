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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.ChapterBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;
import com.wooki.links.impl.ViewLink;

/**
 * Display a chapter and provide link to version/revision if needed.
 * 
 * @author ccordenier
 */
public class Index extends ChapterBase
{

    @Inject
    private ChapterManager chapterManager;

    @Property
    private Long previous;

    @Property
    private String previousTitle;

    @Property
    private Long next;

    @Property
    private String nextTitle;

    @Property
    private PageLink left;

    @Property
    private PageLink right;

    @Property
    private PageLink center;

    @SetupRender
    public Object setupDisplay()
    {
        this.setupContent();

        if (!this.isViewingRevision())
        {
            // Prepare previous and next links
            Object[] data = this.chapterManager.findPrevious(this.getBookId(), getChapterId());
            if (data != null && data.length == 2)
            {
                this.previous = (Long) data[0];
                this.previousTitle = (String) data[1];
            }

            data = this.chapterManager.findNext(this.getBookId(), getChapterId());
            if (data != null && data.length == 2)
            {
                this.next = (Long) data[0];
                this.nextTitle = (String) data[1];
            }
        }

        return null;

    }

    @SetupRender
    public void setupNav()
    {
        selectPublic(0);
        
        if ((previous != null) && (previousTitle != null))
        {
            left = new NavLink("chapter/index", "nav-left", previousTitle, getBookId(), previous);
        }
        else
        {
            left = new ViewLink("book/index", "toc", getBookId());
        }
        if ((next != null) && (nextTitle != null))
        {
            right = new NavLink("chapter/index", "nav-right", nextTitle, getBookId(), next);
        }
        center = new NavLink("book/index", "book-root", getBook().getTitle(), getBookId());
    }

    public String getTitle()
    {
        return getBook().getTitle() + " - " + getChapter().getTitle();
    }

    /**
     * Get context for previous link.
     * 
     * @return Book id, previous chapter id and revision
     */
    public Object[] getPreviousCtx()
    {
        if (this.isViewingRevision()) { return new Object[]
        { this.getBookId(), this.previous, ChapterManager.LAST }; }
        return new Object[]
        { this.getBookId(), this.previous };
    }

    /**
     * Get context for next link.
     * 
     * @return Book id, previous chapter id and revision
     */
    public Object[] getNextCtx()
    {
        if (this.isViewingRevision()) { return new Object[]
        { this.getBookId(), this.next, ChapterManager.LAST }; }
        return new Object[]
        { this.getBookId(), this.next };
    }

    public boolean isShowAdmin()
    {
        return !this.isViewingRevision() || ChapterManager.LAST.equals(this.getRevision());
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Object[] retrieveBookId()
    {
        if (this.getRevision() != null) { return new Object[]
        { this.getBookId(), getChapterId(), this.getRevision() }; }
        return new Object[]
        { this.getBookId(), getChapterId() };
    }

}
