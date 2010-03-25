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

package com.wooki.pages.dev;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.wooki.BookMenuItem;

public class BookIndex
{
    @Inject
    @Symbol(SymbolConstants.PRODUCTION_MODE)
    private boolean productionMode;

    @Property
    private List<BookMenuItem> menu;

    @Property
    private List<BookMenuItem> adminActions;

    @OnEvent(value = EventConstants.ACTIVATE)
    private Object redirect()
    {
        adminActions = new ArrayList<BookMenuItem>();
        menu = new ArrayList<BookMenuItem>();

        if (productionMode) return com.wooki.pages.Index.class;

        return null;
    }
}
