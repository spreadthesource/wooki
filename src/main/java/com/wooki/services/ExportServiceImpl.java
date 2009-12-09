package com.wooki.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.services.parsers.Convertor;

public class ExportServiceImpl implements ExportService {

	private BookManager bookManager;

	private ChapterManager chapterManager;

	private Convertor toFOConvertor;

	private Convertor toPDFConvertor;

	private Convertor toXHTMLConvertor;

	private Convertor toImprovedXHTMLConvertor;

	public InputStream exportPdf(Long bookId) {
		if (bookId == null) {
			throw new IllegalArgumentException(
					"Book id cannot be null to export.");
		}

		/** Generate XHTML from book */
		Book b = bookManager.findById(bookId);
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html><head><title>").append(b.getTitle()).append(
				"</title></head>");
		buffer.append("<body>");
		List<Chapter> chapters = chapterManager.listChapters(bookId);
		if (chapters != null) {
			for (Chapter c : chapters) {
				buffer.append("<h1>").append(c.getTitle()).append("</h1>");
				String content = chapterManager.getLastPublishedContent(c
						.getId());
				if (content != null) {
					buffer.append(content);
				}
			}
		}
		buffer.append("</body></html>");

		/** Generate PDF */
		Resource resource = new ByteArrayResource(buffer.toString().getBytes());
		InputStream xhtml = toXHTMLConvertor.performTransformation(resource);
		InputStream improvedXhtml = toImprovedXHTMLConvertor.performTransformation(new InputStreamResource(xhtml));
		InputStream fo = toFOConvertor
				.performTransformation(new InputStreamResource(improvedXhtml));
		InputStream pdf = toPDFConvertor
				.performTransformation(new InputStreamResource(fo));
		return pdf;
	}

	public BookManager getBookManager() {
		return bookManager;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}

	public ChapterManager getChapterManager() {
		return chapterManager;
	}

	public void setChapterManager(ChapterManager chapterManager) {
		this.chapterManager = chapterManager;
	}

	public Convertor getToFOConvertor() {
		return toFOConvertor;
	}

	public void setToFOConvertor(Convertor toFOConvertor) {
		this.toFOConvertor = toFOConvertor;
	}

	public Convertor getToPDFConvertor() {
		return toPDFConvertor;
	}

	public void setToPDFConvertor(Convertor toPDFConvertor) {
		this.toPDFConvertor = toPDFConvertor;
	}

	public Convertor getToXHTMLConvertor() {
		return toXHTMLConvertor;
	}

	public void setToXHTMLConvertor(Convertor toXHTMLConvertor) {
		this.toXHTMLConvertor = toXHTMLConvertor;
	}

	public Convertor getToImprovedXHTMLConvertor() {
		return toImprovedXHTMLConvertor;
	}

	public void setToImprovedXHTMLConvertor(Convertor toXHTMLConvertor) {
		this.toImprovedXHTMLConvertor = toXHTMLConvertor;
	}

}
