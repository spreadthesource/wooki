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

package com.wooki.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;
import com.wooki.services.utils.SqlUtils;

/**
 * Basic implementation of SearchService.
 * 
 * @author ccordenier
 */
public class SimpleSearchEngineImpl implements SearchEngine
{

    @Inject @Autowired
    private BookManager bookManager;

    public List<Book> findBook(String queryString)
    {
        // Check empty query string
        if (queryString == null || "".equals(queryString.trim())) { return new ArrayList<Book>(); }
        return bookManager.listByTitle("%" + SqlUtils.escapeWildcards(queryString) + "%");
    }

}
