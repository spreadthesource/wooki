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

package com.wooki.pages.book;

import java.util.List;
import java.util.ListIterator;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;
import com.wooki.links.impl.ViewLink;

/**
 * Display all the comment for a given chapter.
 * 
 * @author ccordenier
 */
public class Issues extends BookBase
{

    @Inject
    private ChapterManager chapterManager;

    @Property
    private List<Chapter> chapters;

    @Property
    private Chapter chapter;

    @Property
    private int loopIdx;

    @Property
    private PageLink left;

    @Property
    private PageLink center;

    private Long chapterId;

    @SetupRender
    public void setupNav()
    {
        left = new ViewLink("book/index", "toc", getBookId());
        center = new NavLink("book/index", "book-root", getBook().getTitle(), getBookId());
    }

    @Override
    @SetupRender
    public void setupMenus()
    {
        super.setupMenus();
        selectMenuItem(1);
    }

    /**
     * Prepare the list of comments to display, only published chapter can have user comments.
     * 
     * @param bookId
     * @return
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupComments(Long bookId)
    {
        this.chapters = this.chapterManager.listChaptersInfo(bookId);
        ListIterator<Chapter> it = chapters.listIterator();
        while (it.hasNext())
        {
            Chapter n = it.next();
            if (!isPublished(n.getId()))
            {
                it.remove();
            }
        }
        return null;
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Object[] retrieveBookId()
    {
        return new Object[]
        { this.getBookId() };
    }

    public boolean isPublished(Long chapterId)
    {
        Publication publication = chapterManager.getLastPublishedPublication(chapterId);

        return publication != null;
    }

}
