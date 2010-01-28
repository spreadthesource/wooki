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

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.services.RequestExceptionHandler;

import com.wooki.WookiSymbolsConstants;

/**
 * Tapestry module use for tapestry overrides or customization.
 */
public class TapestryOverrideModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(RequestExceptionHandler.class, WookiRequestExceptionHandler.class).withId("WookiRequestExceptionHandler");
	}

	/**
	 * Wooki Symbols default
	 */
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(WookiSymbolsConstants.ERROR_WOOKI_EXCEPTION_REPORT, "error/generic");
	}

	/**
	 * Alias default request handler.
	 * 
	 * @param exceptionHandler
	 * @param configuration
	 */
	public static void contributeAlias(@InjectService("WookiRequestExceptionHandler") RequestExceptionHandler exceptionHandler,
			Configuration<AliasContribution> configuration) {
		configuration.add(AliasContribution.create(RequestExceptionHandler.class, exceptionHandler));
	}

}
