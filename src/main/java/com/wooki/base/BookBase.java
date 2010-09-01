package com.wooki.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Publication;
import com.wooki.services.HttpError;
import com.wooki.services.utils.DateUtils;

/**
 * Abstract index page to share common code.
 * 
 * @author ccordenier
 */
public class BookBase
{

    @Inject
    private BookManager bookManager;

    @Inject
    @Path("context:/static/img/rss_15x15.png")
    private Asset rssLogo;

    @InjectPage
    private com.wooki.pages.book.Index bookIndex;

    @Property
    private SimpleDateFormat format = DateUtils.getDateFormat();

    @Property
    private DateFormat sinceFormat = DateUtils.getSinceDateFormat();

    private Publication publication;

    private Book book;

    private Long bookId;

    private String content;

    private Long publicationId;

    private boolean viewingRevision;

    private String revision;

    private boolean resourceNotFound;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupBookBase(Long bookId)
    {
        this.bookId = bookId;

        // Check resource exists
        this.book = this.bookManager.findById(this.bookId);

        if (this.book == null)
        {
            resourceNotFound = true;
            return new HttpError(404, "Resource Not Found");
        }

        return null;
    }

    @SetupRender
    public Object checkResource()
    {
        if (this.resourceNotFound) { return false; }
        return null;
    }

    /**
     * Return true if this is the last row.
     * 
     * @param idx
     * @param maxIdx
     * @return
     */
    public boolean isLastIteration(int idx, int maxIdx)
    {
        return idx == maxIdx - 1;
    }

    /**
     * Verify if this is the antepenultiem iteration.
     * 
     * @param idx
     * @param maxIdx
     * @return
     */
    public boolean isAntepenultiemIteration(int idx, int maxIdx)
    {
        return idx == maxIdx - 2;
    }

    /**
     * This method must be called to initialize the display of a chapter. It can be the abstract for
     * the index page or the content of a chapter.
     * 
     * @param revision
     *            TODO
     */
    protected void setupContent()
    {

        // Get the publication
        if (publication != null)
        {
            this.content = publication.getContent();
            this.publicationId = publication.getId();
        }

    }

    protected final Object redirectToBookIndex()
    {
        bookIndex.setBookId(this.bookId);
        return bookIndex;
    }

    public Asset getRssLogo()
    {
        return rssLogo;
    }

    public Long getBookId()
    {
        return bookId;
    }

    public void setBookId(Long bookId)
    {
        this.bookId = bookId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getPublicationId()
    {
        return publicationId;
    }

    public void setPublicationId(Long publicationId)
    {
        this.publicationId = publicationId;
    }

    public boolean isViewingRevision()
    {
        return viewingRevision;
    }

    public void setViewingRevision(boolean viewRevision)
    {
        this.viewingRevision = viewRevision;
    }

    public String getRevision()
    {
        return revision;
    }

    public void setRevision(String revision)
    {
        this.revision = revision;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public boolean isLast()
    {
        return ChapterManager.LAST.equals(this.getRevision());
    }

    public Publication getPublication()
    {
        return publication;
    }

    public void setPublication(Publication publication)
    {
        this.publication = publication;
    }

}
