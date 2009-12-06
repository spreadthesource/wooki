package com.wooki.components.security;

import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Verify if a user has logged in.
 *
 * @author ccordenier
 *
 */
public class IfLoggedIn extends AbstractConditional {

	@Inject
	private WookiSecurityContext securityContext;

	/**
	 * @return test parameter (if negate is false), or test parameter inverted
	 *         (if negate is true)
	 */
	protected boolean test() {
		return securityContext.isLoggedIn();
	}

}
