package com.wooki.services.utils;

/**
 * Static class for SQL string manipulation
 * 
 * @author ccordenier
 * 
 */
public class SqlUtils {

	/**
	 * By now only escape 
	 *
	 * @param query
	 * @return
	 */
	public static String escapeWildcards(String query) {
		String result = query.replace("!", "!!").replace("%", "!%").replace("_", "!_");
		return result;
	}

}
