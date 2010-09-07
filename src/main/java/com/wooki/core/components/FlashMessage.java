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

package com.wooki.core.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library =
{ "context:/static/js/jquery.notifyBar.js", "context:/static/js/notifybar.js" }, stylesheet =
{ "context:/static/css/jquery.notifyBar.css" })
public class FlashMessage
{

    @Parameter(autoconnect = true, required = true)
    private String message;

    @Inject
    private JavaScriptSupport support;

    private String flashMessageId;

    boolean beginRender(MarkupWriter writer)
    {

        if ("".equals(message) || message == null) { return false; }

        flashMessageId = support.allocateClientId("flash-msg");

        writer.element("div", "style", "display:none;", "id", flashMessageId);

        writer.element("div", "class", "flash-list shadowed");
        writer.element("ul", "class", "wrapper");

        writer.element("li");
        writer.write(message);
        writer.end(); // li
        writer.end(); // ul
        writer.end(); // div
        writer.end(); // div

        return true;
    }

    // Add javascript
    void afterRender()
    {
        if (flashMessageId != null)
        {
            support.addInitializerCall("initFlashMsgBox", this.flashMessageId);
        }
        message = null;
    }

}
