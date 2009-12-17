package com.wooki.pages.book;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Delegate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.pages.chapter.Edit;
import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.ExportService;
import com.wooki.services.utils.DateUtils;

/**
 * This page displays a book with its table of contents.
 */
public class Index {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private WookiSecurityContext securityContext;

	@Inject
	private ExportService exportService;

	@Property
	private Book book;

	@Property
	private Long bookAbstractId;

	@Property
	private Long publicationId;

	@Property
	private User currentUser;

	@Property
	private int loopIdx;

	@Property
	private SimpleDateFormat format = DateUtils.getDateFormat();

	@Property
	private String abstractContent;

	@Property
	private List<User> authors;

	@Property
	private List<Chapter> chaptersInfo;

	@Property
	private Chapter currentChapter;

	/**
	 * Will be set if the author tries to add a new chapter
	 */
	@Property
	private String chapterName;

	@Property
	private Block addChapterToggle;

	@Inject
	private Block addChapterLink;

	@Inject
	private Block addChapterForm;

	@Component
	private Delegate addChapterDelegate;

	@InjectPage
	private Edit editChapter;

	private Long bookId;

	private boolean showWorkingCopyLink;

	private boolean bookAuthor;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {

		// Check parameter numbers
		if (ctx.getCount() == 0) {
			return com.wooki.pages.Index.class;
		}

		this.bookId = ctx.get(Long.class, 0);

		// Get book related information
		this.book = bookManager.findById(bookId);
		this.authors = book.getAuthors();

		// List chapter infos
		List<Chapter> chapters = chapterManager.listChaptersInfo(bookId);
		this.bookAbstractId = chapters.get(0).getId();

		if (chapters.size() > 0) {
			this.chaptersInfo = chapters.subList(1, chapters.size());
		}

		Publication published = this.chapterManager.getLastPublishedPublication(this.bookAbstractId);

		if (published != null) {
			this.publicationId = published.getId();
			this.abstractContent = this.chapterManager.getLastPublishedContent(this.bookAbstractId);
		}

		bookAuthor = securityContext.isAuthorOfBook(bookId);
		if (bookAuthor) {
			this.addChapterToggle = this.addChapterLink;

		}

		return null;
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "addChapterForm")
	public Object addNewChapter() {
		Chapter chapter = bookManager.addChapter(book, chapterName);
		editChapter.setBookId(bookId);
		editChapter.setChapterId(chapter.getId());
		return editChapter;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long retrieveBookId() {
		return bookId;
	}

	@OnEvent(value = EventConstants.ACTION, component = "showAddChapterField")
	public Object toggleAddChapter() {
		this.addChapterToggle = addChapterForm;
		return addChapterDelegate;
	}

	/**
	 * Simply export to PDF.
	 * 
	 * @return
	 */
	@OnEvent(value = "print")
	public StreamResponse exportPdf() {
		return new StreamResponse() {

			public void prepareResponse(Response response) {
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Expires", "max-age=0");
				response.setHeader("Content-Disposition", "inline; filename=" + book.getSlugTitle() + ".pdf");
			}

			public InputStream getStream() throws IOException {
				return exportService.exportPdf(bookId);
			}

			public String getContentType() {
				return "application/pdf";
			}
		};
	}

	public boolean isPublished() {
		long chapterId = currentChapter.getId();

		Publication publication = this.chapterManager.getLastPublishedPublication(chapterId);

		return (publication != null);
	}

	public boolean isShowWorkingCopyLink() {
		long chapterId = currentChapter.getId();

		Publication publication = this.chapterManager.getLastPublication(chapterId);

		boolean workingCopy = !publication.isPublished();

		return bookAuthor && workingCopy;
	}

	/**
	 * Get edit context for chapter
	 * 
	 * @return
	 */
	public Object[] getEditCtx() {
		return new Object[] { this.bookId, this.bookAbstractId };
	}

	/**
	 * Get id to link to chapter display
	 * 
	 * @return
	 */
	public Object[] getChapterCtx() {
		return new Object[] { this.bookId, this.currentChapter.getId() };
	}

	public Object[] getChapterWorkingCopyCtx() {
		return new Object[] { this.bookId, this.currentChapter.getId(), "workingcopy" };
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	/**
	 * Used to verify if it's the last elt in the list.
	 * 
	 * @return
	 */
	public boolean isNotLastAuthor() {
		if (loopIdx < this.authors.size() - 1) {
			return true;
		}
		return false;
	}

}
