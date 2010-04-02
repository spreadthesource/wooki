package com.spreadthesource.tapestry5.installer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.ServletContextSymbolProvider;
import org.apache.tapestry5.internal.TapestryAppInitializer;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.internal.services.MapSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.ServletApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spreadthesource.tapestry5.installer.services.InstallerModule;

public class TapestryDelayedFilter implements Filter
{

    private static final String TAPESTRY_INSTALLER_PACKAGE = "tapestry.installer-package";

    private final Logger logger = LoggerFactory.getLogger(TapestryFilter.class);

    private FilterConfig config;

    private Registry registry;

    private boolean installed;

    private HttpServletRequestHandler handler;

    /**
     * Key under which that Tapestry IoC {@link org.apache.tapestry5.ioc.Registry} is stored in the
     * ServletContext. This allows other code, beyond Tapestry, to obtain the Registry and, from it,
     * any Tapestry services. Such code should be careful about invoking
     * {@link org.apache.tapestry5.ioc.Registry#cleanupThread()} appropriately.
     */
    public static final String REGISTRY_CONTEXT_NAME = "org.apache.tapestry5.application-registry";

    /**
     * Initializes the filter using the {@link TapestryAppInitializer}. The application name is the
     * capitalization of the filter name (as specified in web.xml).
     */
    public final void init(FilterConfig filterConfig) throws ServletException
    {
        config = filterConfig;

        ServletContext context = config.getServletContext();

        String filterName = config.getFilterName();

        SymbolProvider scprovider = new ServletContextSymbolProvider(context);

        // TODO throw exception if missing configuration
        Map<String, String> configuration = new HashMap<String, String>();
        configuration.put(InternalConstants.DISABLE_DEFAULT_MODULES_PARAM, "true");
        configuration.put(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM, scprovider
                .valueForSymbol(TAPESTRY_INSTALLER_PACKAGE));
        SymbolProvider provider = new MapSymbolProvider(configuration);

        String executionMode = System.getProperty("tapestry.execution-mode", "production");

        TapestryAppInitializer appInitializer = new TapestryAppInitializer(logger, provider,
                filterName, "servlet", executionMode);

        appInitializer.addModules(InstallerModule.class);

        registry = appInitializer.createRegistry();

        context.setAttribute(REGISTRY_CONTEXT_NAME, registry);

        ServletApplicationInitializer ai = registry.getService(
                "ServletApplicationInitializer",
                ServletApplicationInitializer.class);

        ai.initializeApplication(filterConfig.getServletContext());

        registry.performRegistryStartup();

        handler = registry.getService("HttpServletRequestHandler", HttpServletRequestHandler.class);

        init(registry);

        appInitializer.announceStartup();
    }

    protected final FilterConfig getFilterConfig()
    {
        return config;
    }

    /**
     * Invoked from {@link #init(FilterConfig)} after the Registry has been created, to allow any
     * additional initialization to occur. This implementation does nothing, and my be overriden in
     * subclasses.
     * 
     * @param registry
     *            from which services may be extracted
     * @throws ServletException
     */
    protected void init(Registry registry) throws ServletException
    {

    }

    /**
     * Overridden in subclasses to provide additional module definitions beyond those normally
     * located. This implementation returns an empty array.
     */
    protected ModuleDef[] provideExtraModuleDefs(ServletContext context)
    {
        return new ModuleDef[0];
    }

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {

        if (!installed)
        {
            if (((HttpServletRequest) request).getServletPath().contains("restart"))
            {
                synchronized (this)
                {
                    this.restart();
                }

                ((HttpServletResponse) response).sendRedirect("/wooki/");
            }
            else
            {
                try
                {
                    boolean handled = handler.service(
                            (HttpServletRequest) request,
                            (HttpServletResponse) response);
                }
                finally
                {
                    registry.cleanupThread();
                }
            }
            return;
        }

        chain.doFilter(request, response);

    }

    /**
     * Shuts down and discards the registry. Invokes
     * {@link #destroy(org.apache.tapestry5.ioc.Registry)} to allow subclasses to peform any
     * shutdown logic, then shuts down the registry, and removes it from the ServletContext.
     */
    public final void destroy()
    {
        destroy(registry);

        registry.shutdown();

        config.getServletContext().removeAttribute(REGISTRY_CONTEXT_NAME);

        registry = null;
        config = null;
        handler = null;
    }

    /**
     * Switch to the real context.
     */
    private void restart() throws ServletException
    {
        // Shutdown current registry.
        this.registry.shutdown();

        // Instantiate a new one
        ServletContext context = config.getServletContext();

        String filterName = config.getFilterName();

        SymbolProvider provider = new ServletContextSymbolProvider(context);

        String executionMode = System.getProperty("tapestry.execution-mode", "production");

        TapestryAppInitializer appInitializer = new TapestryAppInitializer(logger, provider,
                filterName, "servlet", executionMode);

        appInitializer.addModules(provideExtraModuleDefs(context));

        registry = appInitializer.createRegistry();

        context.setAttribute(REGISTRY_CONTEXT_NAME, registry);

        ServletApplicationInitializer ai = registry.getService(
                "ServletApplicationInitializer",
                ServletApplicationInitializer.class);

        ai.initializeApplication(this.config.getServletContext());

        registry.performRegistryStartup();

        handler = registry.getService("HttpServletRequestHandler", HttpServletRequestHandler.class);

        init(registry);

        appInitializer.announceStartup();

        this.installed = true;
    }

    /**
     * Invoked from {@link #destroy()} to allow subclasses to add additional shutdown logic to the
     * filter. The Registry will be shutdown after this call. This implementation does nothing, and
     * may be overridden in subclasses.
     * 
     * @param registry
     */
    protected void destroy(Registry registry)
    {

    }
}
