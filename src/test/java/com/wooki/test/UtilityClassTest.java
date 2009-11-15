package com.wooki.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.wooki.services.utils.LastActivityMessage;

/**
 * Use this class to implement utility class test.
 * 
 * @author ccordenier
 * 
 */
public class UtilityClassTest extends Assert {

	@Test
	public void testLastActivityMessage() {

		String message = LastActivityMessage.getActivityPeriod(System
				.currentTimeMillis());
		assertTrue(message.contains("few seconds"));

		message = LastActivityMessage.getActivityPeriod(System
				.currentTimeMillis() - 61000);
		assertTrue(message.contains("minute(s) ago"));

		message = LastActivityMessage.getActivityPeriod(System
				.currentTimeMillis()
				- (61 * 60000));
		assertTrue(message.contains("hour(s) ago"));

		message = LastActivityMessage.getActivityPeriod(System
				.currentTimeMillis()
				- (61 * 60000 * 25));
		assertTrue(message.contains("day(s) ago"));
		
		message = LastActivityMessage.getActivityPeriod(System
				.currentTimeMillis()
				- (61 * 60000 * 25 * 5));
		assertTrue(!message.contains("ago"));

	}

}
