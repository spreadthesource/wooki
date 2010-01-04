package com.wooki.mixins;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Add confirm dialog box.
 *
 * @author ccordenier
 *
 */
@MixinAfter
public class Confirm {

	@Inject
	private RenderSupport support;
	
	@InjectContainer
	private AbstractLink lnk;
	
	@AfterRender
	public void addConfirm() {
		support.addInit("initConfirm",lnk.getClientId());
	}
	
}
