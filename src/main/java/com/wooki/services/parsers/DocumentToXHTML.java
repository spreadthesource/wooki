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

/*
 * $Id: DocumentToXHTML.java,v 1.6 2002/08/09 14:28:44 bveracht Exp $
 * $Log: DocumentToXHTML.java,v $
 * Revision 1.6  2002/08/09 14:28:44  bveracht
 * Added a XML header for the clean.xhtml file.
 * This way, the &nbsp; bug disappears.
 *
 * Revision 1.5  2002/04/11 15:07:41  bveracht
 * Clean up
 *
 */
package com.wooki.services.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

/*
 * This class cleans an HTML file to produce XHTML
 */
public class DocumentToXHTML implements Convertor {

	private boolean xmlOut;
	private Logger logger = Logger.getLogger(DocumentToXHTML.class);

	public InputStream performTransformation(Resource xmlDocument) {
		BufferedInputStream in;
		BufferedOutputStream out;
		ByteArrayOutputStream result;
		Tidy tidy = new Tidy();
		java.util.Properties props = new java.util.Properties();

		props
				.setProperty("new-inline-tags",
						"page-break,page-number,page-numbers,atos,xsl:value-of,xsl:for-each");
		props.setProperty("new-blocklevel-tags",
				"for,page-header,page-footer,xsl:value-of,xsl:for-each");
		props.setProperty("new-empty-tags",
				"page-break,page-number,page-numbers,xsl:value-of");
		// props.setProperty("new-pre-tags", "for,header,footer");
		props.setProperty("new-pre-tags", "atos");
		tidy.setConfigurationFromProps(props);
		// tidy.setDocType("omit");
		tidy.setXmlOut(xmlOut);
		tidy.setXHTML(true);
		tidy.setEmacs(true);
		tidy.setErrfile("tidyErrors.txt");
		tidy.setFixBackslash(true);
		tidy.setNumEntities(true);
		tidy.setQuoteNbsp(false);
		tidy.setCharEncoding(Configuration.LATIN1);
		// tidy.setInputEncoding("ISO-8859-2");
		tidy.setFixComments(true);
		tidy.setQuoteAmpersand(false);
		tidy.setEncloseText(true);
		tidy.setEncloseBlockText(true);
		// tidy.setWord2000(true);

		try {
			tidy.setErrout(new PrintWriter(new FileWriter("tidyErrors.txt"),
					true));

			in = new BufferedInputStream(xmlDocument.getInputStream());
			out = new BufferedOutputStream(result = new ByteArrayOutputStream());
			byte[] XMLHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
					.getBytes();
			out.write(XMLHeader, 0, XMLHeader.length);
			tidy.parse(in, out);
			return new ByteArrayInputStream(result.toByteArray());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error(ioe.getLocalizedMessage());
			return null;
		}
	}

}
