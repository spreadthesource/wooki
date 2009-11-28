package com.wooki.app0.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;

/**
 * Simply throws {@link NullPointerException}
 * 
 * @author ccordenier
 * 
 */
public class ThrowNPE {

	@OnEvent(value = EventConstants.ACTIVATE)
	public void throwNPE() {
		throw new NullPointerException();
	}

}
