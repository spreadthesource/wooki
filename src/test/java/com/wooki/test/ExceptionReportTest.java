package com.wooki.test;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.app0.services.AppModule;

public class ExceptionReportTest {

	private PageTester pageTester;

	@BeforeClass
	public void setup() {
		pageTester = new PageTester("com.wooki.app0", "app", "src/test/app0",
				AppModule.class);
	}

	@Test
	public void verifyWookiExceptionHandling() {
		Document document = pageTester.renderPage("ThrowIAE");
		Assert.assertNotNull(document.getElementById("specificReport"),
				"Wiki Exception should not handled IllegalArgumentException");

	}

	@Test
	public void verifyWookiExceptionNonHandling() {
		Document document = pageTester.renderPage("ThrowNPE");
		Assert.assertNull(document.getElementById("specificReport"),
				"Wiki Exception has not handled NullPointerException");
	}
}
