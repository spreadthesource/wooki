package com.wooki.services.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.internal.EmptyEventContext;

/**
 * Object used to store the information related to activation context for a
 * given page.
 * 
 * @author ccordenier
 * 
 */
public class ActivationContextDefinition {

	static final String OBJECT_ARRAY_TYPE = "java.lang.Object[]";

	static final String EVENT_CONTEXT_TYPE = EventContext.class.getName();

	static final String LIST_TYPE = List.class.getName();

	private final static int ANY_NUMBER_OF_PARAMETERS = -1;

	/**
	 * The list of method definition for activation context for this class.
	 */
	private List<Class[]> authorizedActivationContext = new ArrayList<Class[]>();

	/**
	 * Tag this page as unsecured
	 */
	private boolean unsecured;

	/**
	 * Create the activation context definition for the given class.
	 * 
	 * @param clazz
	 */
	public ActivationContextDefinition(Class clazz) {

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {

			OnEvent eventHandler = method.getAnnotation(OnEvent.class);

			if (eventHandler != null && EventConstants.ACTIVATE.equals(eventHandler.value())) {

				// Check parameters
				Class[] parameterType = method.getParameterTypes();
				int count = getParameterCount(parameterType);

				if (ANY_NUMBER_OF_PARAMETERS == count) {
					this.unsecured = true;
					this.authorizedActivationContext.clear();
					break;
				}

				this.authorizedActivationContext.add(parameterType);
			}

		}
	}

	/**
	 * This method must be called to check if the activation context of the
	 * current request correspond to an existing matching method.
	 * 
	 * @param pageName
	 * @param ctx
	 * @return
	 */
	public boolean findMatchingMethod(String pageName, EventContext ctx) {

		// First check unsecure flag
		if (unsecured) {
			return true;
		}

		// No activation context method inside class
		if (this.authorizedActivationContext.size() == 0) {
			return EmptyEventContext.class.equals(ctx.getClass());
		}

		// Check the list of parameter types arrays
		for (Class[] parameterType : this.authorizedActivationContext) {
			if (parameterType.length == ctx.getCount()) {

				// If no parameter to check return true
				if (ctx.getCount() == 0) {
					return true;
				}

				for (int i = 0; i < parameterType.length; i++) {
					Class type = parameterType[i];
					try {
						ctx.get(type, i);
						if (i == parameterType.length - 1) {
							return true;
						}
					} catch (RuntimeException re) {
						break;
					}
				}
			}
		}

		return false;

	}

	private int getParameterCount(Class[] types) {

		if (types.length == 0)
			return 0;

		if (types.length == 1) {
			String soloType = types[0].getName();

			if (soloType.equals(OBJECT_ARRAY_TYPE) || soloType.equals(EVENT_CONTEXT_TYPE) || soloType.equals(LIST_TYPE))
				return ANY_NUMBER_OF_PARAMETERS;
		}

		return types.length;
	}

}
