package com.wooki.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;

/**
 * Generally used with cancel link in forms.
 * 
 * @author ccordenier
 * 
 */
@MixinAfter
public class ResetFormOnClick {

	@Inject
	private RenderSupport renderSupport;

	@Inject
	private FormSupport formSupport;

	@InjectContainer
	private ClientElement container;

	/**
	 * Simply bind the input element to submit the enclosing form on change
	 * event.
	 */
	@AfterRender
	public void addSubmitOnChange() {
		renderSupport.addInit("resetFormOnClick", container.getClientId(), formSupport.getClientId());
	}

}
