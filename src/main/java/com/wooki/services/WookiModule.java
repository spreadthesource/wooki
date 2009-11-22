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
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.services.AssetSource;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.security.userdetails.UserDetailsService;

public class WookiModule {
	
	public static final String SALT = "wookiwooki";
	
	public static void bind(ServiceBinder binder) {
		binder.bind(StartupService.class, StartupServiceImpl.class).eagerLoad();
		binder.bind(UserDetailsService.class, UserDetailsServiceImpl.class);
	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
		configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
		configuration.add("spring-security.failure.url", "/signin/failed");
		configuration.add("spring-security.accessDenied.url", "/signin");
		configuration.add("spring-security.target.url", "/index");
		configuration.add("spring-security.password.salt", SALT);
		configuration.add("spring-security.afterlogout.url", "/signin");
	}

	public static void contributeProviderManager(
			OrderedConfiguration<AuthenticationProvider> configuration,
			@InjectService("DaoAuthenticationProvider") AuthenticationProvider daoAuthenticationProvider) {
		configuration.add("daoAuthenticationProvider",
				daoAuthenticationProvider);
	}

	public static void contributeAlias(
			Configuration<AliasContribution<PasswordEncoder>> configuration) {
		configuration.add(AliasContribution.create(PasswordEncoder.class,
				new ShaPasswordEncoder()));
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
