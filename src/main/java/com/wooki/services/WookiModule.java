package com.wooki.services;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.security.userdetails.UserDetailsService;

import com.wooki.services.internal.TapestryOverrideModule;
import com.wooki.services.security.UserDetailsServiceImpl;

@SubModule(TapestryOverrideModule.class)
public class WookiModule<T> {

	/**
	 * Used to stored the last view page in session.
	 * 
	 */
	public static final String VIEW_REFERER = "tapestry-view.referer";

	/**
	 * Use to encrypt the user password
	 */
	public static final String SALT = "wookiwooki";

	public void contributeApplicationDefaults(
			MappedConfiguration<String, String> conf) {
		conf.add(SymbolConstants.SUPPORTED_LOCALES, "en");
		conf.add(SymbolConstants.PRODUCTION_MODE, "false");
		conf.add(SymbolConstants.APPLICATION_VERSION, "0.1");
	}

	public static void bind(ServiceBinder binder) {
		binder.bind(StartupService.class, StartupServiceImpl.class).eagerLoad();
		binder.bind(UserDetailsService.class, UserDetailsServiceImpl.class);
		binder.bind(SecurityUrlSource.class, SecurityUrlSourceImpl.class);
		binder.bind(WookiViewRefererFilter.class);
	}

	/**
	 * Store the last view page in session.
	 * 
	 */
	public static void contributePageRenderRequestHandler(
			OrderedConfiguration<PageRenderRequestFilter> filters, WookiViewRefererFilter vrFilter) {
		filters.add("ViewRefererFilter", vrFilter);
	}

	/**
	 * Add request that shouldn't generate a referer.
	 *
	 * @param excludePattern
	 */
	public static void contributeWookiViewRefererFilter(Configuration<String> excludePattern) {
		excludePattern.add("signin");
		excludePattern.add("signup");
		excludePattern.add(".*settings.*");
		excludePattern.add(".*edit.*");
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
	public static void adviseClientInfrastructure(
			MethodAdviceReceiver receiver, final AssetSource source)
			throws SecurityException, NoSuchMethodException {

		MethodAdvice advice = new MethodAdvice() {
			public void advise(Invocation invocation) {
				invocation.proceed();
				List<Asset> jsStack = (List<Asset>) invocation.getResult();
				jsStack
						.add(source
								.getClasspathAsset("context:static/js/jquery-1.3.2.min.js"));
				jsStack
						.add(source
								.getClasspathAsset("context:static/js/jquery.noconflict.js"));
				jsStack.add(source
						.getClasspathAsset("context:static/js/wooki.js"));
			}
		};

		receiver.adviseMethod(receiver.getInterface().getMethod(
				"getJavascriptStack"), advice);
	};

}
