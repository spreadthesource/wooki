package com.wooki.pages.dev;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.internal.services.ArrayEventContext;
import org.apache.tapestry5.internal.services.HeartbeatImpl;
import org.apache.tapestry5.internal.services.RenderQueueImpl;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
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

	@Inject
	private RequestPageCache pageCache;

	@Inject
	private TypeCoercer typeCoercer;

	@Inject
	private LoggerSource loggerSource;

	@Inject
	private MarkupWriterFactory factory;

	@Inject
	private Environment environment;

	@Inject
	@Symbol(SymbolConstants.CHARSET)
	private String encoding;

	@OnEvent(value = "print")
	public Object printFromFull() {

		MarkupWriter writer = factory.newMarkupWriter(new ContentType(encoding));

		Page full = pageCache.get("book/full");
		full.getRootElement().triggerContextEvent(EventConstants.ACTIVATE, new ArrayEventContext(typeCoercer, 1), null);
		String name = "wooki.render." + full.getLogger().getName();
		Logger logger = loggerSource.getLogger(name);

		try {

			this.environment.push(Heartbeat.class, new HeartbeatImpl());
			RenderQueueImpl queue = new RenderQueueImpl(logger);
			queue.push(full.getRootElement());
			queue.run(writer);

			ByteArrayOutputStream dos = new ByteArrayOutputStream();
			PrintWriter printWriter = new PrintWriter(dos);
			writer.toMarkup(printWriter);
			dos.flush();
			printWriter.flush();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(new String(dos.toByteArray()).getBytes(encoding)));
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
			throw new TapestryException(ex.getMessage(), ex);
		} finally {
			this.environment.pop(Heartbeat.class);
		}
	}

	public Object testFlyingSaucer() {

		Book b = bookManager.findBookBySlugTitle("the-book-of-wooki");
		StringBuffer buffer = new StringBuffer();

		buffer.append("<html><head><title>").append(b.getTitle()).append("</title>");
		buffer
				.append(
						"<link rel='stylesheet' type='text/css' href='file:/Users/ccordenier/Documents/workspace-tapestry-5.1/wooki/src/main/webapp/static/css/print.css' media='print' />")
				.append("</head>");
		buffer.append("<body>");
		buffer.append("<div style=\"-fs-page-sequence: start; page-break-after: always;\">");
		List<Chapter> chapters = chapterManager.listChapters(b.getId());

		buffer.append("<div id=\"header\" style=\"position: running(current);\"><span>").append(b.getTitle()).append("</span></div>");
		buffer
				.append("<div id=\"title\" style=\"page-break-after: always;\"><h1>")
				.append(b.getTitle())
				.append(
						"</h1><div id=\"meta\"><p id=\"authors\"> By Robin Komiwes, Bruno Verachten and Christophe Cordenier</p><p id=\"revision\">Published 04 february 2010</p></div></div>");
		if (chapters != null) {

			// Append abstract
			if (chapters.size() > 0) {
				Chapter c = chapters.get(0);
				buffer.append("<h2>").append(c.getTitle()).append("</h2>");
				String content = chapterManager.getLastPublishedContent(c.getId());
				if (content != null) {
					buffer.append(content);
				}

				// Build Toc
				buffer.append("<h2>Table of contents</h2>");
				if (chapters.size() > 1) {
					buffer.append("<ol id=\"table-of-contents\">");
					for (int i = 1; i < chapters.size(); i++) {
						c = chapters.get(i);
						buffer.append("<li><h3><a href=\"#chapter" + i + "\">").append(c.getTitle()).append("</a></h3></li>");
					}
					buffer.append("</ol>");
				}

				// Append content
				for (int i = 1; i < chapters.size(); i++) {
					c = chapters.get(i);
					buffer.append("<h2 id=\"chapter" + i + "\">").append(c.getTitle()).append("</h2>");
					content = chapterManager.getLastPublishedContent(c.getId());
					if (content != null) {
						buffer.append(content);
					}
				}

			}

		}
		buffer.append("</div></body></html>");
		
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
