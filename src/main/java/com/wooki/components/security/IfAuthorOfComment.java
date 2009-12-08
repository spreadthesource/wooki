package com.wooki.components.security;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Verify if the current logged user is author of the requested comment.
 *
 * @author ccordenier
 *
 */
public class IfAuthorOfComment extends AbstractConditional {

	@Parameter(required = true, allowNull = false)
	private Long commentId;
	
	@Inject
	private WookiSecurityContext securityContext;

	/**
	 * @return test parameter (if negate is false), or test parameter inverted
	 *         (if negate is true)
	 */
	protected boolean test() {
		return securityContext.isAuthorOfComment(commentId);
	}

}