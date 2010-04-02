package com.spreadthesource.tapestry5.installer;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.services.HttpServletRequestHandler;

public class TapestryTerminatorFilter implements Filter
{
    private HttpServletRequestHandler handler;

    private Registry registry;

    private final Object lock = new Object();

    private ServletContext context;

    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {

        // Lazy init registry
        if (this.registry == null)
        {
            this.registry = (Registry) this.context
                    .getAttribute(TapestryDelayedFilter.REGISTRY_CONTEXT_NAME);
            if (this.registry != null)
            {
                this.handler = this.registry.getService(HttpServletRequestHandler.class);
            }
        }

        // Check if tapestry handler exists
        if (this.handler == null)
        {
            chain.doFilter(request, response);
            return;
        }

        try
        {
            boolean handled = handler.service(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response);

            if (!handled) chain.doFilter(request, response);
        }
        finally
        {
            registry.cleanupThread();
        }

    }

    public void init(FilterConfig config) throws ServletException
    {
        this.context = config.getServletContext();
    }

}
