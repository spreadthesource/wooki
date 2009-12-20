package com.wooki.components.activity;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.domain.model.activity.BookActivity;

/**
 * Display activities.
 * 
 * @author ccordenier
 * 
 */
public class Book {

	@Property
	@Parameter(allowNull = false, required = true)
	private BookActivity activity;

}
