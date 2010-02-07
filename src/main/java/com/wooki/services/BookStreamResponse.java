package com.wooki.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * Can be used to print a book from an action link.
 * 
 * @author ccordenier
 * 
 */
public class BookStreamResponse implements StreamResponse {

	private final InputStream bookStream;

	private final String filename;

	public BookStreamResponse(String filename, InputStream bookStream) {
		this.filename = filename;
		this.bookStream = bookStream;
	}

	public String getContentType() {
		return "application/pdf";
	}

	public InputStream getStream() throws IOException {
		return this.bookStream;
	}

	public void prepareResponse(Response response) {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "max-age=0");
		response.setHeader("Content-Disposition", "attachment; filename=" + this.filename + ".pdf");
	}

}
