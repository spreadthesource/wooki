//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.test.unit;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.app0.services.AppModule;
import com.wooki.services.ServicesMessages;
import com.wooki.services.utils.LastActivityMessages;

/**
 * Use this class to implement utility class test.
 * 
 * @author ccordenier
 * 
 */
public class UtilityClassTest extends Assert {

	private PageTester pageTester;

	@BeforeClass
	public void setup() {
		pageTester = new PageTester("com.wooki.app0", "app", "src/test/app0", AppModule.class);
	}

	@Test
	public void testLastActivityMessage() {

		Messages messages = this.pageTester.getService(ServicesMessages.class).getMessages();

		String message = LastActivityMessages.getActivityPeriod(System.currentTimeMillis(), messages);
		assertTrue(message.contains("few seconds"));

		message = LastActivityMessages.getActivityPeriod(System.currentTimeMillis() - 61000, messages);
		assertTrue(message.contains("minute(s) ago"));

		message = LastActivityMessages.getActivityPeriod(System.currentTimeMillis() - (61 * 60000), messages);
		assertTrue(message.contains("hour(s) ago"));

		message = LastActivityMessages.getActivityPeriod(System.currentTimeMillis() - (61 * 60000 * 25), messages);
		assertTrue(message.contains("day(s) ago"));

		message = LastActivityMessages.getActivityPeriod(System.currentTimeMillis() - (61 * 60000 * 25 * 5), messages);
		assertTrue(!message.contains("ago"));

	}

}
