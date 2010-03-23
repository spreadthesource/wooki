package com.wooki.services.security;

import java.util.Map;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.InvalidationListener;

public class ActivationContextManagerImpl implements ActivationContextManager, InvalidationListener {

	@Inject
	private ComponentSource source;

	/**
	 * This map store all the activation context definition for a given page
	 * 
	 */
	private Map<String, ActivationContextDefinition> definition = CollectionFactory.newCaseInsensitiveMap();

	public boolean checkContext(String pageName, EventContext ctx) {

		Component page = source.getPage(pageName);

		ActivationContextDefinition def = getDefinition(pageName, page.getClass());

		return def.findMatchingMethod(pageName, ctx);
	}

	/**
	 * Get the definition from Map or create it and return it. 
	 *
	 * @param pageName
	 * @param clazz
	 * @return
	 */
	private ActivationContextDefinition getDefinition(String pageName, Class<?> clazz) {

		synchronized (this.definition) {
			if (definition.containsKey(pageName)) {
				return definition.get(pageName);
			}
		}

		ActivationContextDefinition def = new ActivationContextDefinition(clazz);

		synchronized (this.definition) {
			this.definition.put(pageName, def);
		}

		return def;

	}

	public synchronized void objectWasInvalidated() {
		this.definition.clear();
	}
}
