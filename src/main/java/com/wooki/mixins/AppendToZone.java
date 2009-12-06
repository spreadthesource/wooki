package com.wooki.mixins;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Append content instead of replacing it. 
 *
 * @author ccordenier
 *
 */
@MixinAfter
public class AppendToZone {

	@Inject
	private RenderSupport support;
	
	@InjectContainer
	private Zone zone;
	
	@AfterRender
	public void forceAppend() {
		support.addInit("appendToZone", zone.getClientId());
	}
	
}
