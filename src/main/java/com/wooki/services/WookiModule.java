//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.services.ComponentInstanceProcessor;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ComponentClasses;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.EnvironmentalShadowBuilder;
import org.apache.tapestry5.services.InvalidationEventHub;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.Traditional;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.apache.tapestry5.services.messages.PropertiesFileParser;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.wooki.ActivityType;
import com.wooki.AppendPosition;
import com.wooki.WookiSymbolsConstants;
import com.wooki.domain.biz.AclManager;
import com.wooki.domain.biz.AclManagerImpl;
import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.biz.ActivityManagerImpl;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.BookManagerImpl;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.ChapterManagerImpl;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.biz.CommentManagerImpl;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.biz.UserManagerImpl;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.ActivityDAOImpl;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.BookDAOImpl;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.ChapterDAOImpl;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.dao.CommentDAOImpl;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.dao.PublicationDAOImpl;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.dao.UserDAOImpl;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.services.feeds.ActivityFeedWriter;
import com.wooki.services.feeds.FeedModule;
import com.wooki.services.feeds.impl.AccountActivityFeed;
import com.wooki.services.feeds.impl.BookActivityFeed;
import com.wooki.services.feeds.impl.ChapterActivityFeed;
import com.wooki.services.feeds.impl.CommentActivityFeed;
import com.wooki.services.internal.TapestryOverrideModule;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.parsers.DOMManagerImpl;
import com.wooki.services.security.ActivationContextManager;
import com.wooki.services.security.ActivationContextManagerImpl;
import com.wooki.services.security.SecureActivationContextRequestFilter;
import com.wooki.services.security.UserDetailsServiceImpl;
import com.wooki.services.security.WookiSecurityContext;
import com.wooki.services.security.WookiSecurityContextImpl;

@SubModule(
{ TapestryOverrideModule.class, FeedModule.class })
public class WookiModule<T>
{

    /**
     * Used to stored the last view page in session.
     */
    public static final String VIEW_REFERER = "tapestry-view.referer";

    private final InvalidationEventHub classesInvalidationEventHub;

    private final EnvironmentalShadowBuilder environmentalBuilder;

    private final Environment environment;

    public WookiModule(@ComponentClasses InvalidationEventHub classesInvalidationEventHub,
            Environment environment, EnvironmentalShadowBuilder environmentalBuilder)
    {
        this.classesInvalidationEventHub = classesInvalidationEventHub;
        this.environmentalBuilder = environmentalBuilder;
        this.environment = environment;
    }

    public void contributeApplicationDefaults(MappedConfiguration<String, String> conf)
    {
        conf.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        conf.add(SymbolConstants.FORCE_ABSOLUTE_URIS, "true");
        conf.add(SymbolConstants.APPLICATION_VERSION, "0.2.0");
        conf.add(WookiSymbolsConstants.ERROR_WOOKI_EXCEPTION_REPORT, "error/generic");
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(StartupService.class, StartupServiceImpl.class).preventReloading().eagerLoad();
        binder.bind(UserDetailsService.class, UserDetailsServiceImpl.class);
        binder.bind(SecurityUrlSource.class, SecurityUrlSourceImpl.class);
        binder.bind(UploadMediaService.class, UploadMediaServiceImpl.class);
        binder.bind(DOMManager.class, DOMManagerImpl.class);
        binder.bind(WookiViewRefererFilter.class);

        // domain dao
        binder.bind(ActivityDAO.class, ActivityDAOImpl.class);
        binder.bind(BookDAO.class, BookDAOImpl.class);
        binder.bind(ChapterDAO.class, ChapterDAOImpl.class);
        binder.bind(CommentDAO.class, CommentDAOImpl.class);
        binder.bind(PublicationDAO.class, PublicationDAOImpl.class);
        binder.bind(UserDAO.class, UserDAOImpl.class);

        // domain biz
        binder.bind(ActivityManager.class, ActivityManagerImpl.class);
        binder.bind(BookManager.class, BookManagerImpl.class);
        binder.bind(ChapterManager.class, ChapterManagerImpl.class);
        binder.bind(CommentManager.class, CommentManagerImpl.class);
        binder.bind(UserManager.class, UserManagerImpl.class);

    }

    public LinkSupport buildLinkSupport()
    {
        return environmentalBuilder.build(LinkSupport.class);
    }

    /**
     * Build messages catalog service for services.
     */
    public ServicesMessages buildServicesMessages(
            @Symbol(SymbolConstants.APPLICATION_CATALOG) Resource appCatalogResource,
            @Inject ClasspathURLConverter urlConverter, @Inject ThreadLocale locale,
            @Inject LinkSource linkSource, @Inject UpdateListenerHub listenerHub,
            @Inject PropertiesFileParser fileParse)
    {
        ServicesMessages messages = new ServicesMessagesImpl(appCatalogResource, urlConverter,
                locale, linkSource, fileParse);
        listenerHub.addUpdateListener(messages);
        return messages;
    }

    public ActivationContextManager buildActivationContextManager(
            @Autobuild ActivationContextManagerImpl service)
    {
        // This covers invalidations due to changes to classes
        classesInvalidationEventHub.addInvalidationListener(service);

        return service;
    }

    /**
     * Strategy for outputting feed content based on activity
     */
    @SuppressWarnings("unchecked")
    public static ActivityFeedWriter buildActivityFeedWriter(
            Map<Class, ActivityFeedWriter> configuration,
            @InjectService("StrategyBuilder") StrategyBuilder builder)
    {

        StrategyRegistry<ActivityFeedWriter> registry = StrategyRegistry.newInstance(
                ActivityFeedWriter.class,
                configuration);

        return builder.build(registry);
    }

    @SuppressWarnings("unchecked")
    public void contributeActivityFeedWriter(
            MappedConfiguration<Class, ActivityFeedWriter> configuration,
            @Autobuild AccountActivityFeed accountActivityFeed,
            @Autobuild BookActivityFeed bookActivityFeed,
            @Autobuild ChapterActivityFeed chapterActivityFeed,
            @Autobuild CommentActivityFeed commentActivityFeed)
    {
        configuration.add(AccountActivity.class, accountActivityFeed);
        configuration.add(BookActivity.class, bookActivityFeed);
        configuration.add(ChapterActivity.class, chapterActivityFeed);
        configuration.add(CommentActivity.class, commentActivityFeed);
    }

    /**
     * Allow to return error code instance.
     * 
     * @param componentInstanceProcessor
     * @param configuration
     */
    public void contributeComponentEventResultProcessor(
            @Traditional @ComponentInstanceProcessor ComponentEventResultProcessor componentInstanceProcessor,
            MappedConfiguration<Class, ComponentEventResultProcessor> configuration)
    {
        configuration.addInstance(HttpError.class, HttpErrorResultProcessor.class);
    }

    /**
     * Add a filter to secure activation context in request.
     * 
     * @param filters
     * @param manager
     * @param response
     */
    public static void contributeComponentRequestHandler(
            OrderedConfiguration<ComponentRequestFilter> filters, ActivationContextManager manager,
            Response response, MultipartDecoder decoder)
    {
        filters.add("secureActivationContextFilter", new SecureActivationContextRequestFilter(
                manager, response, decoder));
    }

    /**
     * Wooki Symbols default
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(
                WookiSymbolsConstants.ERROR_UNHANDLED_BROWSER_PAGE,
                "error/unhandledbrowser");
        
        configuration.add(WookiSymbolsConstants.MIGRATIONS_PATH, "WEB-INF/migrations/");
    }

    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration,
            @Inject @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM) String rootPackage)
    {
        configuration.add(rootPackage + ".domain.model");
    }

    public static void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration)
    {
        configuration.addInstance("UploadedAsset", UploadedAssetDispatcher.class, "before:Asset");
    }

    /**
     * Contribute GAnalytics plugin to append google analytics javascript to generated pages.
     * 
     * @param configuration
     * @param scriptInjector
     * @param productionMode
     * @param environment
     * @param clientInfrastructure
     */
    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
            @Symbol(SymbolConstants.PRODUCTION_MODE) final boolean productionMode,
            final PageRenderLinkSource pageLinkSource, final LinkSource linkSource,
            final RequestPageCache pageCache)
    {
        // Add general links support
        configuration.add("MenuSupport", new MarkupRendererFilter()
        {
            public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer)
            {
                RenderSupport renderSupport = environment.peek(RenderSupport.class);
                LinkSupport linkSupport = new LinkSupportImpl(linkSource, pageLinkSource,
                        pageCache, renderSupport);
                environment.push(LinkSupport.class, linkSupport);
                try
                {
                    renderer.renderMarkup(writer);
                    linkSupport.commit(writer);
                }
                finally
                {
                    environment.pop(LinkSupport.class);
                }
            }
        }, "after:RenderSupport");

    }

    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryConfiguration", new ClasspathResourceSymbolProvider(
                "config/tapestry.properties"));
        providers.add("springSecurity", new ClasspathResourceSymbolProvider(
                "config/security.properties"));
    }

    /**
     * Store the last view page in session.
     */
    public static void contributePageRenderRequestHandler(
            OrderedConfiguration<PageRenderRequestFilter> filters, WookiViewRefererFilter vrFilter)
    {
        filters.add("ViewRefererFilter", vrFilter);
    }

    /**
     * Add coercion tuple for parameter types...
     * 
     * @param configuration
     */
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration)
    {
        addTuple(configuration, String.class, ActivityType.class, StringToEnumCoercion
                .create(ActivityType.class));
        addTuple(configuration, String.class, AppendPosition.class, StringToEnumCoercion
                .create(AppendPosition.class));
    }

    /**
     * Add request that shouldn't generate a referer.
     * 
     * @param excludePattern
     */
    public static void contributeWookiViewRefererFilter(Configuration<String> excludePattern)
    {
        excludePattern.add("signin");
        excludePattern.add("signup");
        excludePattern.add(".*edit.*");
        excludePattern.add("dev.*");
        excludePattern.add("error.*");
    }

    /**
     * Add jQuery in no conflict mode to default JavaScript Stack
     * 
     * @param receiver
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @SuppressWarnings("unchecked")
    @Match("ClientInfrastructure")
    public static void adviseClientInfrastructure(MethodAdviceReceiver receiver,
            final AssetSource source) throws SecurityException, NoSuchMethodException
    {

        MethodAdvice advice = new MethodAdvice()
        {
            public void advise(Invocation invocation)
            {
                invocation.proceed();
                List<Asset> jsStack = (List<Asset>) invocation.getResult();
                jsStack.add(source.getClasspathAsset("context:static/js/jquery-1.3.2.min.js"));
                jsStack.add(source.getClasspathAsset("context:static/js/jquery.noconflict.js"));
                jsStack.add(source.getClasspathAsset("context:static/js/wooki.js"));
            }
        };

        receiver.adviseMethod(receiver.getInterface().getMethod("getJavascriptStack"), advice);
    }

    @Match("*Manager")
    public static void adviseTransactions(HibernateTransactionAdvisor advisor,
            MethodAdviceReceiver receiver)
    {
        advisor.addTransactionCommitAdvice(receiver);
    }

    private static <S, T> void addTuple(Configuration<CoercionTuple> configuration,
            Class<S> sourceType, Class<T> targetType, Coercion<S, T> coercion)
    {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }

}
