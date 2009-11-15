package com.wooki.services.utils;

import org.apache.commons.httpclient.HostConfiguration;

public class MyHostConfiguration extends HostConfiguration {

	private final String DELIMITER = ":";

	public synchronized String getProxyHostPlusPort() {

		return new StringBuffer(getProxyHost()).append(DELIMITER).append(
				getProxyPort()).toString();
	}

	public synchronized void setProxyHostPlusPort(String proxyHostPlusPort) {

		if (null == proxyHostPlusPort)
			throw new IllegalArgumentException(
					"proxyHostPlusPort string cannot be null");
		if (proxyHostPlusPort.indexOf(DELIMITER) == -1)
			throw new IllegalArgumentException(
					"proxyHostPlusPort string is expected in 'host:port' format");

		String[] hostPlusPort = proxyHostPlusPort.split(DELIMITER);

		setProxy(hostPlusPort[0], new Integer(hostPlusPort[1]).intValue());
	}
}