package com.wooki.services.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;

/**
 * Utility class used to display elements in the last activity window.
 * 
 * @author ccordenier
 * 
 */
public class LastActivityMessage {

	private final static Messages MESSAGES = MessagesImpl.forClass(LastActivityMessage.class);
	
	/**
	 * Return a string (human) representation of the period since the last
	 * activity.
	 * 
	 * @param lastActivity
	 * @return
	 */
	public static String getActivityPeriod(long lastActivity) {

		String result = "";
		DateFormat formatter = new SimpleDateFormat(MESSAGES.get("date"));
		
		long now = System.currentTimeMillis();

		long period = (now - lastActivity) / 1000;

		if (period < 60) {
			result = MESSAGES.get("few-seconds-ago");
		} else {
			if ((period / 60) < 60) {
				result = String.format(MESSAGES.get("minutes-ago"), (period / 60));
			} else {
				if ((period / 60 / 60) < 24) {
					result = String.format(MESSAGES.get("hours-ago"),
							(period / 60 / 60));
				} else {
					if ((period / 60 / 60 / 24) < 5) {
						result = String.format(MESSAGES.get("days-ago"),
								(period / 60 / 60 / 24));
					} else {
						result = formatter.format(new Date(lastActivity));
					}
				}
			}
		}

		return result;
	}

}
