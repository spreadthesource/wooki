package com.wooki.services;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import com.wooki.WookiSymbolsConstants;

/**
 * Some browsers (as Internet Explorer family) are still not supported by wooki.
 * We have to redirect them to an error page
 */
public class UnhandledBrowserDispatcher implements Dispatcher
{
    private static final EventContext EMPTY_CONTEXT = new EmptyEventContext();

    private final ComponentClassResolver componentClassResolver;

    private final ComponentEventLinkEncoder linkEncoder;

    private final PageRenderRequestHandler handler;

    private final String unhandledBrowserPage;

    private final PageRenderRequestParameters parameters;

    public UnhandledBrowserDispatcher(ComponentClassResolver componentClassResolver,

    PageRenderRequestHandler handler, ComponentEventLinkEncoder linkEncoder,

    @Inject @Symbol(WookiSymbolsConstants.UNHANDLED_BROWSER_PAGE) String unhandledBrowserPage)
    {
	this.componentClassResolver = componentClassResolver;
	this.linkEncoder = linkEncoder;
	this.handler = handler;
	this.unhandledBrowserPage = unhandledBrowserPage;

	parameters = new PageRenderRequestParameters(this.unhandledBrowserPage, EMPTY_CONTEXT);
    }

    public boolean dispatch(Request request, Response response) throws IOException
    {
	// depending your webserver, to get the user agent, you need to test
	// both user-agent and User-Agent
	String userAgent = request.getHeader("user-agent");

	if (userAgent == null || userAgent.equals(""))
	{
	    userAgent = request.getHeader("User-Agent");
	}

	// Internet Explorer patterns
	// IE5: Mozilla/4.0 (compatible; MSIE 5.0; Windows NT;)
	// IE6: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR
	// 1.1.4322; .NET CLR 2.0.50727; InfoPath.1; .NET CLR 3.0.04506.30; .NET
	// CLR 3.0.04506.648; AskTB5.5)
	// IE7: Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; en-US)
	// IE8: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.2; Trident/4.0;
	// SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729;
	// Media Center PC 6.0)

	Pattern p = Pattern.compile(".*compatible; MSIE .?\\..*");
	Matcher m = p.matcher(userAgent);

	if (m.matches() && componentClassResolver.isPageName(this.unhandledBrowserPage))
	{

	    PageRenderRequestParameters initialParameters = linkEncoder.decodePageRenderRequest(request);

	    if (initialParameters == null)
		return false;

	    handler.handle(parameters);

	    return true;
	}

	return false;
    }

}
