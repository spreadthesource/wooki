package com.wooki.components;

import org.apache.tapestry5.MarkupWriter;
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

	@BeginRender
	public void displayDate(MarkupWriter writer) {
		writer.write(LastActivityMessage.getActivityPeriod(time));
	}
	
}
