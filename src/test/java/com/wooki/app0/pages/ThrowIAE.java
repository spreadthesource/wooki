package com.wooki.app0.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;

/**
 * Simply throw {@link IllegalArgumentException}
 * 
 * @author ccordenier
 * 
 */
public class ThrowIAE {

	@OnEvent(value = EventConstants.ACTIVATE)
	public void throwIAE() {
		throw new IllegalArgumentException();
	}

}
