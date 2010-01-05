package com.wooki.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * Used to print a book from an action link. 
 *
 * @author ccordenier
 *
 */
public class BookStreamResponse implements StreamResponse {

	private final ExportService exportSvc;
	
	private final Long bookId;
	
	private final String filename;
	
	public BookStreamResponse(ExportService exportSvc, Long bookId, String filename) {
		this.exportSvc = exportSvc;
		this.bookId = bookId;
		this.filename = filename;
	}
	
	public String getContentType() {
		return "application/pdf";
	}

	public InputStream getStream() throws IOException {
		return this.exportSvc.exportPdf(this.bookId);
	}

	public void prepareResponse(Response response) {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "max-age=0");
		response.setHeader("Content-Disposition", "attachment; filename=" + this.filename + ".pdf");
	}

}
