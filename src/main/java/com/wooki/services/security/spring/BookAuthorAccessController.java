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

package com.wooki.services.security.spring;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.springframework.beans.factory.annotation.Autowired;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Secure the access to the book settings
 * 
 * @author ccordenier
 */
public class BookAuthorAccessController implements TapestryResourceAccessController
{

    private final WookiSecurityContext ctx;

    @Inject
    @Autowired
    private BookManager bookManager;

    public BookAuthorAccessController(WookiSecurityContext ctx)
    {
        super();
        this.ctx = ctx;
    }

    /**
     * First will simply check activation context, and that the user is owner of the book.
     */
    public boolean isViewAuthorized(PageRenderRequestParameters params)
    {
        EventContext activationContext = params.getActivationContext();
        if (activationContext.getCount() > 0)
        {
            Long bookId = null;
            Book book = null;
            try
            {
                bookId = activationContext.get(Long.class, 0);
                book = this.bookManager.findById(bookId);
                if (book == null) { return false; }
            }
            catch (RuntimeException re)
            {
                return false;
            }
            return ctx.canWrite(book);
        }
        return false;
    }

    /**
     * First will simply check activation context, and that the user is owner of the book.
     */
    public boolean isActionAuthorized(ComponentEventRequestParameters params)
    {
        EventContext activationContext = params.getPageActivationContext();
        if (activationContext.getCount() > 0)
        {
            Long bookId = null;
            Book book = null;
            try
            {
                bookId = activationContext.get(Long.class, 0);
                book = this.bookManager.findById(bookId);
                if (book == null) { return false; }
            }
            catch (RuntimeException re)
            {
                return false;
            }
            return ctx.canWrite(book);
        }
        return false;
    }

}
