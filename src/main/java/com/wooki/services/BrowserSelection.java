package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import com.wooki.pages.IESupport;

/**
 * @author ccordenier
 */
public class BrowserSelection implements PageRenderRequestFilter
{

    @Inject
    private Response response;

    @Inject
    private Request request;

    @Inject
    private PageRenderLinkSource linkSource;

    public void handle(PageRenderRequestParameters parameters, PageRenderRequestHandler handler)
            throws IOException
    {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null)
        {
            boolean isMsie = userAgent.toLowerCase().contains(" msie ");

            if (isMsie && !parameters.getLogicalPageName().equalsIgnoreCase("IESupport"))
            {
                response.sendRedirect(linkSource.createPageRenderLink(IESupport.class));
            }
        }

        handler.handle(parameters);

    }

}
