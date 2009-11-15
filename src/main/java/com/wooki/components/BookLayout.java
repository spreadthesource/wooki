package com.wooki.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class BookLayout
{
	@Parameter(defaultPrefix="literal")
	@Property
	private String title;
}
