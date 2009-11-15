package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.services.FormSupport;

public class Errors {

	// Allow null so we can generate a better error message if missing
	@Environmental(false)
	private ValidationTracker tracker;

	@Environmental
	private FormSupport formSupport;

	void beginRender(MarkupWriter writer) {

		List<String> errors = tracker.getErrors();

		if (!errors.isEmpty()) {

			writer.element("div", "class", "wooki-form-error");

			// Only write out the <UL> if it will contain <LI> elements. An
			// empty <UL> is not
			// valid XHTML.

			writer.element("ul");

			for (String message : errors) {
				writer.element("li");
				writer.write(message);
				writer.end();
			}

			writer.end(); // ul

			writer.end(); // div
		}

	}

}
