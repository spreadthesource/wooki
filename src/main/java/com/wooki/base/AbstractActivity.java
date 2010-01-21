package com.wooki.base;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * Base class for activity components.
 * 
 * @author ccordenier
 * 
 */
public class AbstractActivity {

	@Parameter
	private boolean resourceAvailable;
	
	@Property
	@Parameter(value = "")
	private String style;

}
