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

package com.wooki.components;

import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;

import com.wooki.services.utils.DateUtils;
import com.wooki.services.utils.LastActivityMessages;

/**
 * Display a date using with a more user friendly format.
 * 
 * @author ccordenier
 * 
 */
public class LastModifiedFormatter {

	@Parameter(required = true, allowNull = false)
	private long time;

	@Parameter(value = "date", defaultPrefix = BindingConstants.LITERAL)
	private String className;
	
	@BeginRender
	public void displayDate(MarkupWriter writer) {
		writer.element("abbr", "title", DateUtils.getLastModified().format(new Date(time)), "class", className);
		writer.write(LastActivityMessages.getActivityPeriod(time));
	}

	@AfterRender
	public void endAbbr(MarkupWriter writer) {
		writer.end();
	}

}
