package com.wooki.base;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.components.BookMenuItem;
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
 * 
 */
public class BookBase {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private LinkSource linkSource;

	@InjectPage
	private com.wooki.pages.book.Index bookIndex;

	@Property
	private SimpleDateFormat format = DateUtils.getDateFormat();

	@Property
	private DateFormat sinceFormat = DateUtils.getSinceDateFormat();

	private BookMenuItem left;

	private BookMenuItem center;

	private BookMenuItem right;

	private Publication publication;

	private Book book;

	private Long bookId;

	private String content;

	private Long publicationId;

	private boolean viewingRevision;

	private String revision;

	private boolean resourceNotFound;

	private List<BookMenuItem> menu;

	private List<BookMenuItem> adminActions;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBookBase(Long bookId) throws IOException {
		this.bookId = bookId;

		// Check resource exists
		this.book = this.bookManager.findById(this.bookId);

		if (this.book == null) {
			resourceNotFound = true;
			return new HttpError(404, "Resource Not Found");
		}

		return null;
	}

	@SetupRender
	public Object checkResource() {
		if (this.resourceNotFound) {
			return false;
		}
		return null;
	}

	@SetupRender
	public void initMenus() {
		this.adminActions = new ArrayList<BookMenuItem>();
		this.menu = new ArrayList<BookMenuItem>();
	}

	/**
	 * Return true if this is the last row.
	 * 
	 * @param idx
	 * @param maxIdx
	 * @return
	 */
	public boolean isLastIteration(int idx, int maxIdx) {
		return idx == maxIdx - 1;
	}

	/**
	 * Verify if this is the antepenultiem iteration.
	 * 
	 * @param idx
	 * @param maxIdx
	 * @return
	 */
	public boolean isAntepenultiemIteration(int idx, int maxIdx) {
		return idx == maxIdx - 2;
	}

	/**
	 * This method must be called to initialize the display of a chapter. It can
	 * be the abstract for the index page or the content of a chapter.
	 * 
	 * @param revision
	 *            TODO
	 * 
	 */
	protected void setupContent() {

		// Get the publication
		if (publication != null) {
			this.content = publication.getContent();
			this.publicationId = publication.getId();
		}

	}

	protected final Object redirectToBookIndex() {
		bookIndex.setBookId(this.bookId);
		return bookIndex;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}

	public boolean isViewingRevision() {
		return viewingRevision;
	}

	public void setViewingRevision(boolean viewRevision) {
		this.viewingRevision = viewRevision;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isLast() {
		return ChapterManager.LAST.equals(this.getRevision());
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public List<BookMenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<BookMenuItem> menu) {
		this.menu = menu;
	}

	public List<BookMenuItem> getAdminActions() {
		return adminActions;
	}

	public void setAdminActions(List<BookMenuItem> adminActions) {
		this.adminActions = adminActions;
	}

	protected final BookMenuItem createPageMenuItem(String name, String pageName, boolean override, Object... pageActivationContext) {
		Link link = linkSource.createPageRenderLink(pageName, override, pageActivationContext);
		return new BookMenuItem(name, link);
	}

	protected final BookMenuItem createEventMenuItem(String name, Page page, String nestedId, String eventType, boolean forForm, Object... context) {
		Link link = linkSource.createComponentEventLink(page, nestedId, eventType, forForm, context);
		return new BookMenuItem(name, link);
	}

	public BookMenuItem getLeft() {
		return left;
	}

	public void setLeft(BookMenuItem left) {
		this.left = left;
	}

	public BookMenuItem getCenter() {
		return center;
	}

	public void setCenter(BookMenuItem center) {
		this.center = center;
	}

	public BookMenuItem getRight() {
		return right;
	}

	public void setRight(BookMenuItem right) {
		this.right = right;
	}

	public void addFeedLink(String title, Link feedLink, MarkupWriter writer) {
		Element head = writer.getDocument().find("html/head");
		head.element("link", "type", "application/atom+xml", "title", title, "rel", "alternate", "href", feedLink.toAbsoluteURI());
	}
}
