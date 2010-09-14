package com.wooki.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Chapter;
import com.wooki.links.Link;
import com.wooki.links.PageLink;
import com.wooki.links.impl.DeleteChapterLink;
import com.wooki.links.impl.EditLink;
import com.wooki.links.impl.ViewLink;
import com.wooki.services.HttpError;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Extend this class to design a page in pages.book package.
 * 
 * @author ccordenier
 */
public class ChapterBase extends PageBase
{
    @Inject
    private ChapterManager chapterManager;

    @Inject
    private WookiSecurityContext securityCtx;

    private Chapter chapter;

    private Long chapterId;

    @Property
    private List<Link> publicLinks;

    @Property
    private List<Link> adminLinks;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String[] errors;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupChapter(Long bookId, Long chapterId)
    {
        // Get book related information
        this.chapterId = chapterId;
        this.chapter = this.chapterManager.findById(chapterId);
        if (this.chapter == null) { return new HttpError(404, "Chapter not found"); }

        this.setPublication(this.chapterManager.getLastPublishedPublication(chapterId));
        if (this.getPublication() == null) { return new HttpError(404, "Chapter not found"); }

        return null;
    }

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupChapter(Long bookId, Long chapterId, String revision)
    {
        this.setViewingRevision(true);
        this.setRevision(revision);

        // Setup chapter
        setupChapter(bookId, chapterId);

        if (ChapterManager.LAST.equalsIgnoreCase(revision)
                && !(this.securityCtx.isLoggedIn() && this.securityCtx.canWrite(this.getBook()))) { return new HttpError(
                403, "Access denied"); }

        this.setPublication(this.chapterManager.getRevision(chapterId, revision));
        if (this.getPublication() == null) { return new HttpError(404, "Revision not found"); }

        return true;
    }

    @SetupRender
    public void setupMenus()
    {
        adminLinks = new ArrayList<Link>();
        publicLinks = new ArrayList<Link>();

        adminLinks.add(new EditLink(getBook(), "chapter/edit", "edit-content", getBookId(),
                chapterId));
        adminLinks.add(new DeleteChapterLink(chapter, "delete-chapter"));

        publicLinks.add(new ViewLink("chapter/index", "chapter-home", getBookId(), chapterId));
        publicLinks.add(new ViewLink("chapter/history", "history", getBookId(), chapterId));
        publicLinks.add(new ViewLink(chapter, "chapter/issues", "all-feedback", false, getBookId(),
                chapterId));
        publicLinks.add(new ViewLink("chapter/rss", "rss-feed", getBookId(), chapterId));
    }

    @OnEvent(value = "delete")
    public Object deleteChapter()
    {
        this.chapterManager.remove(getChapterId());
        return this.redirectToBookIndex();
    }

    public void selectAdmin(int idx)
    {
        PageLink link = (PageLink) adminLinks.get(idx);
        link.setSelected(true);
    }

    public void selectPublic(int idx)
    {
        PageLink link = (PageLink) publicLinks.get(idx);
        link.setSelected(true);
    }

    public Chapter getChapter()
    {
        return chapter;
    }

    public void setChapter(Chapter chapter)
    {
        this.chapter = chapter;
    }

    public Long getChapterId()
    {
        return chapterId;
    }

    public void setChapterId(Long chapterId)
    {
        this.chapterId = chapterId;
    }

}
