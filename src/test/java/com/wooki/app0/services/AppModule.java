package com.wooki.app0.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.mockito.Mockito;

import com.wooki.services.TapestryOverrideModule;

/**
 * Test application module.
 * 
 * @author ccordenier
 * 
 */
@SubModule(TapestryOverrideModule.class)
public class AppModule {

	public void contributeWookiRequestExceptionHandler(
			MappedConfiguration<Class, String> exceptionMap) {
		exceptionMap.add(IllegalArgumentException.class, "IAEReport");
	}

	/**
	 * Use to allow pageTester to run with spring and spring-security.
	 * 
	 * @param config
	 * @param requestGlobals
	 */
	public static void contributeRequestHandler(
			OrderedConfiguration<RequestFilter> config,
			final RequestGlobals requestGlobals) {
		RequestFilter filter = new RequestFilter() {
			public boolean service(Request request, Response response,
					RequestHandler handler) throws IOException {
				requestGlobals.storeServletRequestResponse(Mockito
						.mock(HttpServletRequest.class), Mockito
						.mock(HttpServletResponse.class));
				return handler.service(request, response);
			}
		};
		config.add("EnsureNonNullHttpRequestAndResponse", filter, "before:*");
	}
}
