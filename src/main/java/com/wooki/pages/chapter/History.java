package com.wooki.pages.chapter;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.exception.PublicationXmlException;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.links.Link;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;
import com.wooki.links.impl.ViewLink;
import com.wooki.services.HttpError;

public class History extends BookBase
{
    @Inject
    private ChapterManager chapterManager;

    @Inject
    private Messages messages;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String message;

    @Property
    private List<Publication> publications;

    @Property
    private Publication publication;

    @Property
    private List<Link> publicLinks;

    @Property
    private List<Link> adminLinks;

    @Property
    private Chapter chapter;

    @Property
    private PageLink center;

    private Long chapterId;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupChapter(Long bookId, Long chapterId)
    {
        this.setupBookBase(bookId);

        // Get book related information
        this.chapterId = chapterId;
        this.chapter = this.chapterManager.findById(chapterId);
        if (this.chapter == null) { return new HttpError(404, "Chapter not found"); }

        return true;
    }

    @SetupRender
    public void prepareDisplay()
    {
        // Get publication
        this.publications = chapterManager.listPublicationInfo(chapterId);
    }

    /**
     * Setup all the menu items.
     */
    @SetupRender
    public void setupMenus()
    {
        adminLinks = new ArrayList<Link>();
        publicLinks = new ArrayList<Link>();

        publicLinks.add(new ViewLink("chapter/history", "history", getBookId(), chapterId));
        publicLinks.add(new ViewLink("chapter/issues", "all-feedback", getBookId(), Issues.ALL));
        publicLinks.add(new ViewLink(chapter, "chapter/issues", "chapter-feedback", false,
                getBookId(), chapterId));
    }

    @SetupRender
    public void setupNav()
    {
        center = new NavLink("book/index", "book-root", getBook().getTitle(), getBookId());
    }

    @OnEvent(value = "restore")
    public void restorePublication(String revision)
    {
        try
        {
            chapterManager.restoreRevision(chapterId, revision);
            message = messages.get("revision-restored");
        }
        catch (ConcurrentModificationException cmEx)
        {
            message = messages.get("revision-restore-error");
        }
        catch (PublicationXmlException pxEx)
        {
            message = messages.get("revision-restore-error");
        }
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Object[] retrieveIds()
    {
        return new Object[]
        { getBookId(), chapterId };
    }
}
