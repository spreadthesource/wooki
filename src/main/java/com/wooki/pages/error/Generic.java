package com.wooki.pages.error;

import org.apache.tapestry5.Link;

import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.pages.Index;

@IncludeStylesheet("context:static/css/reset.css")
public class Generic implements ExceptionReporter
{
    @Inject
    private PageRenderLinkSource linkSource;

    /**
     * Return to page index without context.
     */
    public Link getIndexPage()
    {
	return linkSource.createPageRenderLinkWithContext(Index.class, new Object[0]);
    }

	public void reportException(Throwable exception) {
		// TODO : log the exception. Send an alert by email to the admin?
		
	}

}
