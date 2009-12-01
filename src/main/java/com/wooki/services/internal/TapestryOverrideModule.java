package com.wooki.services.internal;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.services.RequestExceptionHandler;



/**
 * Tapestry module use for tapestry overrides or customization.
 *
 * @author ccordenier
 *
 */
public class TapestryOverrideModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(RequestExceptionHandler.class,
				WookiRequestExceptionHandler.class).withId(
				"WookiRequestExceptionHandler");
	}

	/**
	 * Alias default request handler.
	 * 
	 * @param exceptionHandler
	 * @param configuration
	 */
	public static void contributeAlias(
			@InjectService("WookiRequestExceptionHandler") RequestExceptionHandler exceptionHandler,
			Configuration<AliasContribution> configuration) {
		configuration.add(AliasContribution.create(
				RequestExceptionHandler.class, exceptionHandler));
	}

}
