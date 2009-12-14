package com.wooki.components;

import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;

import com.wooki.services.utils.LastActivityMessage;

/**
 * Display a date using with a more user friendly format.
 * 
 * @author ccordenier
 * 
 */
public class LastModifiedFormatter {

	@Parameter(required = true, allowNull = false)
	private long time;

	@Parameter(value = "date", defaultPrefix = BindingConstants.LITERAL)
	private String className;
	
	@BeginRender
	public void displayDate(MarkupWriter writer) {
		writer.element("abbr", "title", new Date(time), "class", className);
		writer.write(LastActivityMessage.getActivityPeriod(time));
	}

	@AfterRender
	public void endAbbr(MarkupWriter writer) {
		writer.end();
	}

}
