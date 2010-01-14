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

	@Property
	@Parameter(value = "")
	private String style;

}
