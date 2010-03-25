package com.wooki.pages.error;

import org.apache.tapestry5.Link;

import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.pages.Index;

@IncludeStylesheet("context:static/css/reset.css")
public class UnhandledBrowser
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

}
