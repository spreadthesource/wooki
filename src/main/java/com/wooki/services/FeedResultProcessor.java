package com.wooki.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

/**
 * Used to serialize a feed object to the client.
 * 
 * @author ccordenier
 * 
 */
public class FeedResultProcessor implements ComponentEventResultProcessor<Feed> {

	private final Request request;

	private final Response response;

	private final String outputEncoding;

	public FeedResultProcessor(Request request, Response response, @Inject @Symbol(SymbolConstants.CHARSET) String outputEncoding) {
		this.response = response;
		this.request = request;
		this.outputEncoding = outputEncoding;
	}

	public void processResultValue(Feed value) throws IOException {

		if (value == null) {
			return;
		}

		request.setAttribute(InternalConstants.SUPPRESS_COMPRESSION, true);

		ContentType contentType = new ContentType("application/atom+xml", value.getEncoding() == null ? outputEncoding : value.getEncoding());

		WireFeedOutput wireFeedOutput = new WireFeedOutput();

		PrintWriter pw = response.getPrintWriter(contentType.toString());

		try {
			wireFeedOutput.output(value, pw);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during feed generation");
			return;
		} catch (FeedException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during feed generation");
			return;
		}

		pw.flush();
	}
}
