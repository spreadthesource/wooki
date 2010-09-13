package com.wooki.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.FeedException;
import com.wooki.links.Link;
import com.wooki.links.PageLink;
import com.wooki.links.impl.EditLink;
import com.wooki.links.impl.ExportLink;
import com.wooki.links.impl.ViewLink;
import com.wooki.services.BookStreamResponse;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.export.ExportService;
import com.wooki.services.feeds.FeedSource;

/**
 * Extend this class to design a page in pages.book package.
 * 
 * @author ccordenier
 */
public class BookBase extends PageBase
{
    @Inject
    private Logger logger;

    @Inject
    private FeedSource feedSource;

    @Inject
    private ExportService exportService;

    @Inject
    private Messages messages;
    
    @Property
    private List<Link> publicLinks;

    @Property
    private List<Link> adminLinks;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String[] errors;

    @SetupRender
    public void setupMenus()
    {
        publicLinks = new ArrayList<Link>();
        adminLinks = new ArrayList<Link>();

        publicLinks.add(new ViewLink("book/index", "book-home", this.getBookId()));
        publicLinks.add(new ViewLink("book/issues", "all-feedback", this.getBookId()));
        publicLinks.add(new ExportLink("print-pdf", "pdf", this.getBookId()));
        publicLinks.add(new ExportLink("rss-feed", "feed", this.getBookId()));

        adminLinks.add(new EditLink(getBook(), "book/settings", "settings", getBookId()));
    }

    public void selectAdmin(int idx)
    {
        PageLink link = (PageLink) adminLinks.get(idx);
        link.setSelected(true);
    }

    public void selectMenuItem(int idx)
    {
        PageLink link = (PageLink) publicLinks.get(idx);
        link.setSelected(true);
    }

    /**
     * Create the Atom feed of the book activity
     * 
     * @throws IOException
     * @throws FeedException
     * @throws IllegalArgumentException
     */
    @OnEvent(value = "feed")
    public Feed getFeed(Long bookId) throws IOException, IllegalArgumentException, FeedException
    {
        return feedSource.produceFeed(ActivitySourceType.BOOK, bookId);
    }

    /**
     * Simply export to PDF.
     * 
     * @return
     */
    @OnEvent(value = "pdf")
    public Object exportPdf()
    {
        try
        {
            InputStream bookStream = this.exportService.exportPdf(this.getBookId());
            return new BookStreamResponse(this.getBook().getSlugTitle(), bookStream);
        }
        catch (Exception ex)
        {
            errors = new String[]
            { messages.get("print-error") };
            logger.error("An Error has occured during PDF generation", ex);
            return this;
        }
    }
}
