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

package com.wooki.services.activity;

import com.wooki.services.EnumService;
import com.wooki.services.activity.impl.AccountActivitySource;
import com.wooki.services.activity.impl.BookActivitySource;
import com.wooki.services.activity.impl.BookCreationActivitySource;
import com.wooki.services.activity.impl.CoAuthorActivitySource;
import com.wooki.services.activity.impl.UserActivitySource;
import com.wooki.services.activity.impl.UserPublicActivitySource;

/**
 * Describe the type of activity to be display by the activity component. So to add a new type of
 * source, you simply have to add the implementation service and declare its corresponding enum key
 * in here.
 * 
 * @author ccordenier
 */
public enum ActivitySourceType implements EnumService<ActivitySource>
{

    /**
     * Use this source to list all the activities of a user, this will obtain everything that has
     * been done by the user. Not only in the public area.
     */
    USER(UserActivitySource.class),

    /**
     * 
     */
    CO_AUTHOR(CoAuthorActivitySource.class),

    /**
     * 
     */
    USER_PUBLIC(UserPublicActivitySource.class),

    /**
     * 
     */
    BOOK_CREATION(BookCreationActivitySource.class),

    /**
     * 
     */
    ACCOUNT(AccountActivitySource.class),

    /**
     * 
     */
    BOOK(BookActivitySource.class);

    @SuppressWarnings("unchecked")
    private Class service;

    /**
     * A type of activity will be automatically associated to a source service class.
     * 
     * @param <T>
     * @param svcClass
     */
    private <T extends ActivitySource> ActivitySourceType(Class<T> svcClass)
    {
        this.service = svcClass;
    }

    @SuppressWarnings("unchecked")
    public Class<ActivitySource> getService()
    {
        return this.service;
    }

    public Class<ActivitySource> getServiceInterface()
    {
        return ActivitySource.class;
    }
    
    
}
