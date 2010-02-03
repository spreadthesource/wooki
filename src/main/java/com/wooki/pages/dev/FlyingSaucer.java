package com.wooki.pages.dev;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;

public class FlyingSaucer {

	@Inject
	private BookManager bookManager;

	@Inject
	private ChapterManager chapterManager;

	@OnEvent(value = "print")
	public Object testFlyingSaucer() {

		Book b = bookManager.findBookBySlugTitle("the-book-of-wooki");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html><head><title>").append(b.getTitle()).append("</title>");
		buffer
				.append(
						"<link rel='stylesheet' type='text/css' href='file:/Users/ccordenier/Documents/workspace-tapestry-5.1/wooki/src/main/webapp/static/css/print.css' media='print'/>")
				.append("</head>");
		buffer.append("<body>");
		List<Chapter> chapters = chapterManager.listChapters(b.getId());
		buffer.append("<h1>").append(b.getTitle()).append("</h1>");
		if (chapters != null) {
			for (Chapter c : chapters) {
				buffer.append("<h2>").append(c.getTitle()).append("</h2>");
				String content = chapterManager.getLastPublishedContent(c.getId());
				if (content != null) {
					buffer.append(content);
				}
			}
		}
		buffer.append("</body></html>");

		// parse our markup into an xml Document
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new StringBufferInputStream(buffer.toString()));
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(doc, null);
			renderer.layout();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			renderer.createPDF(os);
			os.close();

			return new StreamResponse() {

				public void prepareResponse(Response response) {
					response.setHeader("Cache-Control", "no-cache");
					response.setHeader("Expires", "max-age=0");
					response.setHeader("Content-Disposition", "attachment; filename=wooki.pdf");
				}

				public InputStream getStream() throws IOException {
					return new ByteArrayInputStream(os.toByteArray());
				}

				public String getContentType() {
					return "application/pdf";
				}
			};

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;

	}

}
