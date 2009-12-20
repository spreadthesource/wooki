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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;

/**
 * Utility class used to display elements in the last activity window.
 * 
 * @author ccordenier
 * 
 */
public class LastActivityMessage {

	private final static Messages MESSAGES = MessagesImpl.forClass(LastActivityMessage.class);
	
	/**
	 * Return a string (human) representation of the period since the last
	 * activity.
	 * 
	 * @param lastActivity
	 * @return
	 */
	public static String getActivityPeriod(long lastActivity) {

		String result = "";
		DateFormat formatter = new SimpleDateFormat(MESSAGES.get("date"));
		
		long now = System.currentTimeMillis();

		long period = (now - lastActivity) / 1000;

		if (period < 60) {
			result = MESSAGES.get("few-seconds-ago");
		} else {
			if ((period / 60) < 60) {
				result = String.format(MESSAGES.get("minutes-ago"), (period / 60));
			} else {
				if ((period / 60 / 60) < 24) {
					result = String.format(MESSAGES.get("hours-ago"),
							(period / 60 / 60));
				} else {
					if ((period / 60 / 60 / 24) < 5) {
						result = String.format(MESSAGES.get("days-ago"),
								(period / 60 / 60 / 24));
					} else {
						result = formatter.format(new Date(lastActivity));
					}
				}
			}
		}

		return result;
	}

}
