package com.wooki.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.base.WookiBase;

public class BookLayout extends WookiBase {
	@Parameter(defaultPrefix = "literal")
	@Property
	private String title;
}
