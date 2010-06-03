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

package com.wooki.services.internal;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.PageResponseRenderer;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import com.wooki.WookiSymbolsConstants;
import com.wooki.domain.exception.AuthorizationException;

/**
 * Extends default exception handler to allow routing of exception. Default exception page is the
 * Tapestry's default one.
 */
public class WookiRequestExceptionHandler implements RequestExceptionHandler
{
    private Map<Class, String> exceptionMap;

    private final RequestPageCache pageCache;

    private final ComponentClassResolver classResolver;

    private final PageResponseRenderer renderer;

    private final Logger logger;

    private String pageName;

    private final String wookiErrorPage;

    private final Response response;

    private final boolean productionMode;

    public WookiRequestExceptionHandler(
            Map<Class, String> exceptionMap,
            RequestPageCache pageCache,
            ComponentClassResolver classResolver,
            PageResponseRenderer renderer,
            Logger logger,
            @Inject @Symbol(SymbolConstants.EXCEPTION_REPORT_PAGE) String pageName,
            @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
            @Inject @Symbol(WookiSymbolsConstants.ERROR_WOOKI_EXCEPTION_REPORT) String wookiErrorPage,
            Response response)
    {
        this.exceptionMap = exceptionMap;
        this.pageCache = pageCache;
        this.renderer = renderer;
        this.logger = logger;
        this.pageName = pageName;
        this.response = response;
        this.classResolver = classResolver;
        this.productionMode = productionMode;
        this.wookiErrorPage = wookiErrorPage;
    }

    public void handleRequestException(Throwable exception) throws IOException
    {

        String exceptionPage = this.pageName;

        if (this.productionMode)
        {
            exceptionPage = wookiErrorPage;
        }

        logger.error("An exception has occured", exception);

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setHeader("X-Tapestry-ErrorMessage", InternalUtils.toMessage(exception));

        // Access denied when the operation is not authorized
        if (containsException(exception, AuthorizationException.class))
        {
            response.sendError(403, "Access denied");
        }

        // Check if there is an existing a page that correspond to the root
        // exception
        for (Class ex : this.exceptionMap.keySet())
        {
            if (containsException(exception, ex))
            {
                String page = this.exceptionMap.get(ex);
                if (classResolver.isPageName(page))
                {
                    exceptionPage = page;
                    break;
                }
            }
        }

        Page page = pageCache.get(exceptionPage);

        ExceptionReporter rootComponent = (ExceptionReporter) page.getRootComponent();

        // Let the page set up for the new exception.

        rootComponent.reportException(exception);

        renderer.renderPageResponse(page);
    }

    /**
     * Check if the exception stack contains the provided exception type.
     * 
     * @param ex
     * @return
     */
    private <T extends Throwable> boolean containsException(Throwable ex, Class<T> exType)
    {

        boolean result = false;

        do
        {
            if (ex.getClass().equals(exType))
            {
                result = true;
                break;
            }
            ex = ex.getCause();
        }
        while (ex != null);

        return result;

    }
}
