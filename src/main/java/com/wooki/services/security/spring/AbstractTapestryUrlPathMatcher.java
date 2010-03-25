package com.wooki.services.security.spring;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.SessionPersistedObjectAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.ServletContextAware;

/**
 * This is used to identify public pages URLs.
 * 
 * @author ccordenier
 */
public abstract class AbstractTapestryUrlPathMatcher implements WookiPathMatcher,
        ServletContextAware
{

    private ServletContext servletContext;

    private Registry tapestryRegistry;

    private RequestGlobals globals;

    private ComponentEventLinkEncoder encoder;

    private String applicationCharset;

    private SessionPersistedObjectAnalyzer spoa;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String encoding;

    private boolean initDone = false;

    private boolean productionMode;

    /**
     * Initialize Tapestry registry.
     */
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /**
     * Init Tapestry registries and services.
     */
    private void init()
    {
        if (initDone) { return; }
        this.tapestryRegistry = (Registry) this.servletContext
                .getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
        this.encoder = this.tapestryRegistry.getService(ComponentEventLinkEncoder.class);
        this.spoa = this.tapestryRegistry.getService(SessionPersistedObjectAnalyzer.class);
        this.applicationCharset = this.tapestryRegistry.getService(SymbolSource.class)
                .valueForSymbol(SymbolConstants.CHARSET);
        this.globals = this.tapestryRegistry.getService(RequestGlobals.class);
        this.initDone = true;
    }

    /**
     * Hide Tapestry logic for analyzing URLs
     * 
     * @param request
     * @return
     */
    protected ComponentEventRequestParameters decodeComponentEventRequest(String path)
    {
        this.init();
        RequestImpl tapRequest = new RequestImpl(this.createRequestForTapestry(path),
                applicationCharset, spoa);
        globals.storeRequestResponse(tapRequest, null);
        return this.encoder.decodeComponentEventRequest(tapRequest);
    }

    /**
     * Hide Tapestry logic for analyzing URLs
     * 
     * @param request
     * @return
     */
    protected PageRenderRequestParameters decodePageRenderRequest(String path)
    {
        this.init();
        RequestImpl tapRequest = new RequestImpl(this.createRequestForTapestry(path),
                applicationCharset, spoa);
        globals.storeRequestResponse(tapRequest, null);
        return this.encoder.decodePageRenderRequest(tapRequest);
    }

    /**
     * Create a mock http request from a path.
     * 
     * @param path
     * @return
     */
    private HttpServletRequest createRequestForTapestry(String path)
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        int queryIndex = path.indexOf("?");
        if (queryIndex > -1)
        {
            request.setPathInfo(cleanupPath(path.substring(0, queryIndex)));
            String query = path.substring(path.indexOf("?") + 1);
            String[] parameters = query.split("&");
            if (parameters != null)
            {
                for (String parameter : parameters)
                {
                    String[] keyValue = parameter.split("=");
                    if (keyValue != null && keyValue.length == 2)
                    {
                        try
                        {
                            request.addParameter(keyValue[0], URLDecoder.decode(
                                    keyValue[1],
                                    this.encoding));
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            logger.error("Cannot decode URL parameter with " + this.encoding
                                    + " encoding");
                            request.addParameter(keyValue[0], URLDecoder.decode(keyValue[1]));
                        }
                    }
                }
            }
        }
        else
        {
            request.setPathInfo(cleanupPath(path));
        }
        return request;
    }

    /**
     * Simply remove extra character added by servlet container for JSession id.
     * 
     * @param path
     * @return
     */
    private String cleanupPath(String path)
    {
        if (path.contains(";")) { return path.substring(0, path.indexOf(";")); }
        return path;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public boolean isProductionMode()
    {
        return productionMode;
    }

    public void setProductionMode(boolean productionMode)
    {
        this.productionMode = productionMode;
    }

}
