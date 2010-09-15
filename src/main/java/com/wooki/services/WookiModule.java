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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.services.ComponentInstanceProcessor;
import org.apache.tapestry5.internal.services.LinkSource;
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
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ComponentClasses;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.EnvironmentalShadowBuilder;
import org.apache.tapestry5.services.InvalidationEventHub;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.Traditional;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.apache.tapestry5.services.messages.PropertiesFileParser;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.wooki.AppendPosition;
import com.wooki.WookiSymbolsConstants;
import com.wooki.core.services.CoreModule;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.BookManagerImpl;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.ChapterManagerImpl;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.biz.CommentManagerImpl;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.biz.UserManagerImpl;
import com.wooki.services.activity.ActivityModule;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.db.DataModule;
import com.wooki.services.feeds.FeedModule;
import com.wooki.services.internal.TapestryOverrideModule;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.parsers.DOMManagerImpl;
import com.wooki.services.security.ActivationContextManager;
import com.wooki.services.security.ActivationContextManagerImpl;
import com.wooki.services.security.SecureActivationContextRequestFilter;
import com.wooki.services.security.UserDetailsServiceImpl;
import com.wooki.services.security.spring.SecurityUrlSource;
import com.wooki.services.security.spring.SecurityUrlSourceImpl;

@SubModule(
{ TapestryOverrideModule.class, DataModule.class, ActivityModule.class, FeedModule.class,
        CoreModule.class })
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
        conf.add(SymbolConstants.APPLICATION_VERSION, "0.4");
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

        // domain biz
        binder.bind(BookManager.class, BookManagerImpl.class);
        binder.bind(ChapterManager.class, ChapterManagerImpl.class);
        binder.bind(CommentManager.class, CommentManagerImpl.class);
        binder.bind(UserManager.class, UserManagerImpl.class);

        // Bind the service
        binder.bind(EnumServiceLocator.class, EnumServiceLocatorImpl.class);

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
        addTuple(configuration, String.class, ActivitySourceType.class, StringToEnumCoercion
                .create(ActivitySourceType.class));
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
                jsStack.add(0, source.getClasspathAsset("context:static/js/jquery.noconflict.js"));
                jsStack.add(0, source.getClasspathAsset("context:static/js/jquery-1.3.2.min.js"));
                jsStack.add(source.getClasspathAsset("context:static/js/wooki.js"));
                jsStack.add(source.getClasspathAsset("context:static/js/wooki-messages.js"));
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
