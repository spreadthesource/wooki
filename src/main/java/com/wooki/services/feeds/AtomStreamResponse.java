package com.wooki.services.feeds;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class AtomStreamResponse extends ByteArrayOutputStream implements StreamResponse {

    private final String feed;

    public AtomStreamResponse(String feed) {
	this.feed = feed;
    }

    public String getContentType() {
	return "application/atom+xml";
    }

    public InputStream getStream() throws IOException {
	return new ByteArrayInputStream(feed.getBytes());
    }

    public void prepareResponse(Response response) {
    }

}
