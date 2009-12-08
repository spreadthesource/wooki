package com.wooki.pages.book;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.ExportService;
import com.wooki.services.utils.DateUtils;

/**
 * This page displays a book with its table of contents.
 * 
 * @author ccordenier
 * 
 */
public class Index {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

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

	private Long bookId;

	/**
	 * Setup all the data to display in the book index page.
	 * 
	 * @param bookId
	 */
	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupBook(EventContext ctx) {
		
		// Check parameter numbers
		if(ctx.getCount() == 0) {
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

		Publication published = this.chapterManager
				.getLastPublished(this.bookAbstractId);
		if (published != null) {
			this.publicationId = published.getId();
			// Get abstract content to display
			this.abstractContent = this.chapterManager
					.getLastPublishedContent(this.bookAbstractId);
		}

		return null;
	}

	@OnEvent(value = EventConstants.PASSIVATE)
	public Long retrieveBookId() {
		return bookId;
	}

	/**
	 * Simply export to PDF.
	 *
	 * @return
	 */
	@OnEvent(value = "print")
	public StreamResponse exportPdf(){
		return new StreamResponse() {
			
			public void prepareResponse(Response response) {
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Expires", "max-age=0");
				response.setHeader("Content-Disposition", "attachment; "+book.getSlugTitle()+".pdf");
			}
			
			public InputStream getStream() throws IOException {
				return exportService.exportPdf(bookId);
			}
			
			public String getContentType() {
				return "application/pdf";
			}
		};
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
