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

package com.wooki.test.unit;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.app0.services.AppModule;
import com.wooki.installer.services.InstallerModule;
import com.wooki.test.WookiPageTester;

public class ExceptionReportTest
{

    private PageTester pageTester;

    @BeforeClass
    public void setup()
    {
        pageTester = new WookiPageTester("com.wooki.app0", "app", "src/test/app0", AppModule.class, InstallerModule.class);
    }

    @Test
    public void verifyWookiExceptionHandling()
    {
        Document document = pageTester.renderPage("ThrowIAE");
        Assert.assertNotNull(
                document.getElementById("specificReport"),
                "Wiki Exception should handle IllegalArgumentException");

    }

    @Test
    public void verifyWookiExceptionNonHandling()
    {
        Document document = pageTester.renderPage("ThrowNPE");
        Assert.assertNull(
                document.getElementById("specificReport"),
                "Wiki Exception has not handled NullPointerException");
    }

    @AfterClass
    public void cleanup()
    {
        if (pageTester != null)
        {
            pageTester.shutdown();
        }
    }
}
