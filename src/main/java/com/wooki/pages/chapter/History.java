package com.wooki.pages.chapter;

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

import com.wooki.base.ChapterBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.exception.PublicationXmlException;
import com.wooki.domain.model.Publication;
import com.wooki.links.PageLink;
import com.wooki.links.impl.NavLink;

public class History extends ChapterBase
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
    private PageLink center;

    @SetupRender
    public void prepareDisplay()
    {
        // Get publication
        this.publications = chapterManager.listPublicationInfo(getChapter().getId());
    }

    @SetupRender
    public void setupNav()
    {
        selectPublic(1);
        center = new NavLink("book/index", "book-root", getBook().getTitle(), getBookId());
    }

    @OnEvent(value = "restore")
    public void restorePublication(String revision)
    {
        try
        {
            chapterManager.restoreRevision(getChapterId(), revision);
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
        { getBookId(), getChapterId() };
    }
}
