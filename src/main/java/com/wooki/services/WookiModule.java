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

package com.wooki.services;

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
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.springframework.security.userdetails.UserDetailsService;

import com.wooki.ActivityType;
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
			OrderedConfiguration<PageRenderRequestFilter> filters,
			WookiViewRefererFilter vrFilter) {
		filters.add("ViewRefererFilter", vrFilter);
	}

	/**
	 * Add request that shouldn't generate a referer.
	 * 
	 * @param excludePattern
	 */
	public static void contributeWookiViewRefererFilter(
			Configuration<String> excludePattern) {
		excludePattern.add("signin");
		excludePattern.add("signup");
		excludePattern.add(".*settings.*");
		excludePattern.add(".*edit.*");
		excludePattern.add(".*dashboard.*");
	}

	/**
	 * Add coercion tuple for paramter types...
	 *
	 * @param configuration
	 */
	public static void contributeTypeCoercer(
			Configuration<CoercionTuple> configuration) {
		CoercionTuple<String, ActivityType> tuple = new CoercionTuple<String, ActivityType>(
				String.class, ActivityType.class, StringToEnumCoercion
						.create(ActivityType.class));
		configuration.add(tuple);
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
