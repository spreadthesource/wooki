//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
