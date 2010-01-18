package com.wooki.services.impl;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;

import com.wooki.GAnalyticsScriptsMessages;
import com.wooki.WookiSymbolsConstants;
import com.wooki.services.GAnalyticsScriptsInjector;


public class GAnalyticsScriptsInjectorImpl implements GAnalyticsScriptsInjector {

	private final static Messages SCRIPTS = MessagesImpl.forClass(GAnalyticsScriptsMessages.class);

	private final String key;

	public GAnalyticsScriptsInjectorImpl(@Inject @Symbol(WookiSymbolsConstants.GANALYTICS_KEY) String key) {
		this.key = key;
	}

	public void addScript(Document document) {
		if (key != null && !key.isEmpty()) {
			Element root = document.getRootElement();

			if (root == null)
				return;

			Element body = root.find("body");

			if (body == null) {
				body = root.element("body");
			}

			Element e = body.element("script", "type", "text/javascript");

			e.raw(SCRIPTS.get("scriptOne"));

			e = body.element("script", "type", "text/javascript");

			e.raw(SCRIPTS.format("scriptTwo", key));

		}
	}

}
