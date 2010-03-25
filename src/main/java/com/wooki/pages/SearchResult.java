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

package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.SearchEngine;

/**
 * This page search and display a list of wooki elements in function of the activation String.
 * 
 * @author ccordenier
 */
public class SearchResult
{

    @Inject
    private SearchEngine searchService;

    @Property
    // @Validate("regexp=[^%]*")
    private String queryString;

    @Property
    private List<Book> results;

    @Property
    private Book currentBook;

    @Property
    private User currentAuthor;

    @Property
    private int loopIdx;

    /**
     * Prepare search
     * 
     * @param searchField
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public void search(String queryString)
    {
        this.queryString = queryString;
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public String retrieveQuery()
    {
        return this.queryString;
    }

    @SetupRender
    public void setupRender()
    {
        this.results = searchService.findBook(queryString);
    }

    public String getStyle()
    {
        return this.loopIdx == 0 ? "first" : null;
    }

}
