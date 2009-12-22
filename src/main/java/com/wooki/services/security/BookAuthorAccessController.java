package com.wooki.services.security;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * Secure the access to the book settings
 * 
 * @author ccordenier
 * 
 */
public class BookAuthorAccessController implements
		TapestryResourceAccessController {

	private WookiSecurityContext ctx;

	public BookAuthorAccessController(WookiSecurityContext ctx) {
		super();
		this.ctx = ctx;
	}

	/**
	 * First will simply check activation context, and that the user is owner of
	 * the book.
	 * 
	 */
	public boolean isViewAuthorized(PageRenderRequestParameters params) {
		EventContext activationContext = params.getActivationContext();
		if (activationContext.getCount() > 0) {
			Long bookId = null;
			try {
				bookId = activationContext.get(Long.class, 0);
			} catch (RuntimeException re) {
				return false;
			}
			return ctx.isAuthorOfBook(bookId);
		}
		return false;
	}

	/**
	 * First will simply check activation context, and that the user is owner of
	 * the book.
	 * 
	 */
	public boolean isActionAuthorized(ComponentEventRequestParameters params) {
		EventContext activationContext = params.getPageActivationContext();
		if (activationContext.getCount() > 0) {
			Long bookId = null;
			try {
				bookId = activationContext.get(Long.class, 0);
			} catch (RuntimeException re) {
				return false;
			}
			return ctx.isAuthorOfBook(bookId);
		}
		return false;
	}

}
