package com.wooki.services.security.spring;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;
import org.springframework.core.Ordered;

/**
 * Base implementation for spring security filter that requires access to
 * tapestry registry.
 * 
 * @author ccordenier
 * 
 */
public abstract class TapestrySpringSecurityFilter implements Filter, Ordered {

	protected Registry tapestryRegistry;

	protected RequestGlobals globals;

	protected ComponentEventLinkEncoder encoder;

	protected String applicationCharset;

	protected SessionPersistedObjectAnalyzer spoa;

	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Does nothing. We use IoC container lifecycle services instead.
	 * 
	 * @param filterConfig
	 *            ignored
	 * @throws ServletException
	 *             ignored
	 */
	public final void init(FilterConfig filterConfig) throws ServletException {
		ServletContext ctx = filterConfig.getServletContext();
		this.tapestryRegistry = (Registry) ctx.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
		this.encoder = this.tapestryRegistry.getService(ComponentEventLinkEncoder.class);
		this.spoa = this.tapestryRegistry.getService(SessionPersistedObjectAnalyzer.class);
		this.applicationCharset = this.tapestryRegistry.getService(SymbolSource.class).valueForSymbol(SymbolConstants.CHARSET);
		this.globals = this.tapestryRegistry.getService(RequestGlobals.class);
	}

	/**
	 * Does nothing. We use IoC container lifecycle services instead.
	 */
	public final void destroy() {
	}

	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Do we really need the checks on the types in practice ?
		if (!(request instanceof HttpServletRequest)) {
			throw new ServletException("Can only process HttpServletRequest");
		}

		if (!(response instanceof HttpServletResponse)) {
			throw new ServletException("Can only process HttpServletResponse");
		}

		doFilterHttp((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	protected abstract void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

	/**
	 * Hide Tapestry logic for analyzing URLs
	 * 
	 * @param request
	 * @return
	 */
	protected ComponentEventRequestParameters decodeComponentEventRequest(HttpServletRequest request) {
		RequestImpl tapRequest = new RequestImpl(request, applicationCharset, spoa);
		globals.storeRequestResponse(tapRequest, null);
		return this.encoder.decodeComponentEventRequest(tapRequest);
	}

	/**
	 * Hide Tapestry logic for analyzing URLs
	 * 
	 * @param request
	 * @return
	 */
	protected PageRenderRequestParameters decodePageRenderRequest(HttpServletRequest request) {
		RequestImpl tapRequest = new RequestImpl(request, applicationCharset, spoa);
		globals.storeRequestResponse(tapRequest, null);
		return this.encoder.decodePageRenderRequest(tapRequest);
	}

	public String toString() {
		return getClass().getName() + "[ order=" + getOrder() + "; ]";
	}
}