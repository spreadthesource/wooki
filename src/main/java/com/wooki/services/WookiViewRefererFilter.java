package com.wooki.services;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;

/**
 * Add a cookie to store the last permitted view URL.
 * 
 * @author ccordenier
 * 
 */
public class WookiViewRefererFilter implements PageRenderRequestFilter {

	private final Pattern[] ignoredPatterns;

	@Inject
	private Cookies cookieService;

	@Inject
	private RequestGlobals request;

	public WookiViewRefererFilter(Collection<String> configuration) {
		ignoredPatterns = new Pattern[configuration.size()];

		int i = 0;

		for (String regexp : configuration) {
			Pattern p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

			ignoredPatterns[i++] = p;
		}
	}

	public void handle(PageRenderRequestParameters parameters,
			PageRenderRequestHandler handler) throws IOException {
		try {

			// Verify if the cookie must view URL must be registered.
			for (Pattern p : ignoredPatterns) {
				if (p.matcher(parameters.getLogicalPageName()).matches()) {
					return;
				}
			}

			// Write referer cookie value
			cookieService.writeCookieValue(WookiModule.VIEW_REFERER, request
					.getHTTPServletRequest().getRequestURL().toString(), 1800);

		} finally {
			handler.handle(parameters);
		}
	}

}