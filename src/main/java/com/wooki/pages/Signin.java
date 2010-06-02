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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.security.spring.SecurityUrlSource;

/**
 * Login form.
 */
public class Signin
{

    @Inject
    private WookiSecurityContext securityCtx;

    @Inject
    private SecurityUrlSource source;

    @Property
    private String loginUrl;

    private boolean failed = false;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object checkSecurityCtx()
    {
        if (securityCtx.isLoggedIn()) { return Index.class; }
        return null;
    }

    @OnEvent(value = EventConstants.ACTIVATE)
    public void checkError(String extra)
    {
        if (extra.equals("failed"))
        {
            failed = true;
        }
    }

    @SetupRender
    public void setup()
    {
        loginUrl = source.getLoginUrl();
    }

    public boolean isFailed()
    {
        return failed;
    }

    public String[] getMessages()
    {
        return new String[]
        { "Wrong username or password" };
    }
}
