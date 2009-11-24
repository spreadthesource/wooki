package com.wooki.services.utils;

import java.text.SimpleDateFormat;

/**
 * Contains utility classes for Date presentation and handling
 * 
 * @author ccordenier
 * 
 */
public class DateUtils {

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("dd MMMMM yyyy");
	}

}
