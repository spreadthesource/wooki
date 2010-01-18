package com.wooki.services;

import org.apache.tapestry5.dom.Document;

import com.wooki.WookiSymbolsConstants;

/**
 * Automatically adds Google Analytics Tracking code if
 * {@link WookiSymbolsConstants}.GANALYTICS_KEY is set and "production mode" is
 * true
 */
public interface GAnalyticsScriptsInjector {
	public void addScript(Document document);
}
