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