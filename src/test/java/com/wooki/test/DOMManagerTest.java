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

package com.wooki.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.maven.doxia.Converter;
import org.apache.maven.doxia.ConverterException;
import org.apache.maven.doxia.DefaultConverter;
import org.apache.maven.doxia.UnsupportedFormatException;
import org.apache.maven.doxia.wrapper.InputFileWrapper;
import org.apache.maven.doxia.wrapper.OutputFileWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.Comment;
import com.wooki.services.HTMLParser;
import com.wooki.services.parsers.Convertor;
import com.wooki.services.parsers.DOMManager;

/**
 * Test case for DOM manipulation.
 * 
 * @author ccordenier
 * 
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class DOMManagerTest extends AbstractTestNGSpringContextTests {

	private Logger logger = Logger.getLogger(DOMManagerTest.class);

	@Autowired
	private DOMManager generator;

	@Autowired
	@Qualifier("xhtmlToFOConvertor")
	private Convertor toFOConvertor;

	@Autowired
	@Qualifier("xhtmlToAPTConvertor")
	private Convertor toAPTConvertor;

	@Autowired
	@Qualifier("FOToPDFConvertor")
	private Convertor toPDFConvertor;

	@Autowired
	@Qualifier("documentToXHTMLConvertor")
	private Convertor toXHTMLConvertor;

	@Autowired
	@Qualifier("documentToImprovedXHTML4LatexConvertor")
	private Convertor toImprovedXHTML4LatexConvertor;

	@Autowired
	@Qualifier("xhtmlToLatexConvertor")
	private Convertor toLatexConvertor;
	
	@Autowired
	@Qualifier("docbookToXhtmlConvertor")
	private Convertor fromDocbookConvertor;
	
	@Autowired
	@Qualifier("APTHTMLToDocbookHTMLConvertor")
	private Convertor fromAptToDocbook;
	
	@Autowired
	@Qualifier("htmlParser")
	private HTMLParser htmlParser;

	public Convertor getFromDocbookConvertor() {
		return fromDocbookConvertor;
	}

	public void setFromDocbookConvertor(Convertor fromDocbookConvertor) {
		this.fromDocbookConvertor = fromDocbookConvertor;
	}

	public Convertor getToFOConvertor() {
		return toFOConvertor;
	}

	public void setToFOConvertor(Convertor toFOConvertor) {
		this.toFOConvertor = toFOConvertor;
	}

	public Convertor getToXHTMLConvertor() {
		return toXHTMLConvertor;
	}

	public void setToXHTMLConvertor(Convertor toXHTMLConvertor) {
		this.toXHTMLConvertor = toXHTMLConvertor;
	}

	public Convertor getToPDFConvertor() {
		return toPDFConvertor;
	}

	public void setToPDFConvertor(Convertor toPDFConvertor) {
		this.toPDFConvertor = toPDFConvertor;
	}

	public Convertor getToAPTConvertor() {
		return toAPTConvertor;
	}

	public void setToAPTConvertor(Convertor toAPTConvertor) {
		this.toAPTConvertor = toAPTConvertor;
	}

	@Test
	public void testFOConversion() {
		String result = /*
						 * generator .adaptContent(
						 */"<h2>SubTitle</h2><p>Lorem ipsum</p><h3>SubTitle2</h3><p>Lorem ipsum</p>"/* ) */;

		Resource resource = new ByteArrayResource(result.getBytes());
		InputStream xhtml = toXHTMLConvertor.performTransformation(resource);
		logger.debug("Document to xhtml ok");
		InputStream fo = toFOConvertor
				.performTransformation(new InputStreamResource(xhtml));
		logger.debug("xhtml to fo ok");
		InputStream pdf = toPDFConvertor
				.performTransformation(new InputStreamResource(fo));
		logger.debug("fo to pdf ok");
		File pdfFile;
		try {
			pdfFile = File.createTempFile("wooki", ".pdf");
			FileOutputStream fos = new FileOutputStream(pdfFile);
			logger.debug("PDF File is " + pdfFile.getAbsolutePath());
			byte[] content = null;
			int available = 0;
			while ((available = pdf.available()) > 0) {
				content = new byte[available];
				pdf.read(content);
				fos.write(content);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAPTConversion() {
		String result = /*
						 * generator .adaptContent(
						 */"<h2>SubTitle</h2><p>Lorem ipsum</p><h3>SubTitle2</h3><p>Lorem ipsum</p>"/* ) */;

		Resource resource = new ByteArrayResource(result.getBytes());
		InputStream xhtml = toXHTMLConvertor.performTransformation(resource);
		logger.debug("Document to xhtml ok");
		InputStream apt = toAPTConvertor
				.performTransformation(new InputStreamResource(xhtml));
		logger.debug("xhtml to apt ok");
		File aptFile;
		try {
			aptFile = File.createTempFile("wooki", ".apt");
			FileOutputStream fos = new FileOutputStream(aptFile);
			logger.debug("APT File is " + aptFile.getAbsolutePath());
			byte[] content = null;
			int available = 0;
			while ((available = apt.available()) > 0) {
				content = new byte[available];
				apt.read(content);
				fos.write(content);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	@Test
	public void testDocbookConversion() {
		String result = "<book>	  <bookinfo>	    <title>An Example Book</title>	    	    <author>	      <firstname>Your first name</firstname>	      <surname>Your surname</surname>	      <affiliation>	        <address><email>foo@example.com</email></address>	      </affiliation>	    </author>		    <copyright>	      <year>2000</year>	      <holder>Copyright string here</holder>	    </copyright>		    <abstract>	      <para>If your book has an abstract then it should go here.</para>	    </abstract>	  </bookinfo>		  <preface>	    <title>Preface</title>		    <para>Your book may have a preface, in which case it should be placed	      here.</para>	  </preface>	      	  <chapter>	    <title>My First Chapter</title>		    <para>This is the first chapter in my book.</para>		    <sect1>	      <title>My First Section</title>		      <para>This is the first section in my book.</para>	    </sect1>	  </chapter>	</book>";
		Resource resource = new ByteArrayResource(result.getBytes());
		InputStream xhtml = fromDocbookConvertor.performTransformation(resource);
		File htmlFile = null;
		try {
			htmlFile = File.createTempFile("wooki", ".html");
			FileOutputStream fos = new FileOutputStream(htmlFile);
			logger.debug("HTML File is " + htmlFile.getAbsolutePath());
			byte[] content = null;
			int available = 0;
			while ((available = xhtml.available()) > 0) {
				content = new byte[available];
				xhtml.read(content);
				fos.write(content);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return;
		}
		logger.debug("Docbook to xhtml ok");
		SAXParserFactory factory = SAXParserFactory.newInstance();

		// création d'un parseur SAX
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(new InputSource(new FileInputStream(htmlFile)), htmlParser);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}

		Book book = htmlParser.getBook();
		logger.debug("The book title is " + book.getTitle());
	
	}
	
	@Test
	public void testAptConversion() {
		String result = "   ------\n            Title\n            ------\n            Author\n            ------\n             Date\n\n  Paragraph 1, line 1.\n  Paragraph 1, line 2.\n\n  Paragraph 2, line 1.\n  Paragraph 2, line 2.\n\nSection title\n\n* Sub-section title\n\n** Sub-sub-section title\n\n*** Sub-sub-sub-section title\n\n**** Sub-sub-sub-sub-section title\n\n      * List item 1.\n\n      * List item 2.\n\n        Paragraph contained in list item 2.\n\n            * Sub-list item 1.\n\n            * Sub-list item 2.\n\n      * List item 3.\n        Force end of list:\n\n      []\n\n+------------------------------------------+\nVerbatim text not contained in list item 3\n+------------------------------------------+\n\n      [[1]] Numbered item 1.\n\n                [[A]] Numbered item A.\n\n                [[B]] Numbered item B.\n\n      [[2]] Numbered item 2.\n\n  List numbering schemes: [[1]], [[a]], [[A]], [[i]], [[I]].\n\n      [Defined term 1] of definition list.\n\n      [Defined term 2] of definition list.\n\n+-------------------------------+\nVerbatim text\n                        in a box\n+-------------------------------+\n\n  --- instead of +-- suppresses the box around verbatim text.\n\n[Figure name] Figure caption\n\n*----------*--------------+----------------:\n| Centered | Left-aligned | Right-aligned  |\n| cell 1,1 | cell 1,2     | cell 1,3       |\n*----------*--------------+----------------:\n| cell 2,1 | cell 2,2     | cell 2,3       |\n*----------*--------------+----------------:\nTable caption\n\n  No grid, no caption:\n\n*-----*------*\n cell | cell\n*-----*------*\n cell | cell\n*-----*------*\n\n  Horizontal line:\n\n=======================================================================\n\n^L\n  New page.\n\n  <Italic> font. <<Bold>> font. <<<Monospaced>>> font.\n\n  {Anchor}. Link to {{anchor}}. Link to {{http://www.pixware.fr}}.\n  Link to {{{anchor}showing alternate text}}.\n  Link to {{{http://www.pixware.fr}Pixware home page}}.\n\n  Force line\\\n  break.\n\n  Non\\ breaking\\ space.\n\n  Escaped special characters: \\~, \\=, \\-, \\+, \\*, \\[, \\], \\<, \\>, \\{, \\}, \\\\.\n\n  Copyright symbol: \\251, \\xA9, \\u00a9.\n\n~~Commented out.";
		File aptFile = null;
		String from = "apt";
		File out = null;
		
		try {
			out = File.createTempFile("fromAptToXHTML", ".html");
			InputStream apt = new ByteArrayInputStream(result.getBytes());
			try {
				aptFile = File.createTempFile("wooki", ".apt");
				FileOutputStream fos = new FileOutputStream(aptFile);
				logger.debug("APT File is " + aptFile.getAbsolutePath());
				byte[] content = null;
				int available = 0;
				while ((available = apt.available()) > 0) {
					content = new byte[available];
					apt.read(content);
					fos.write(content);
				}
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String to = "xhtml";
			Converter converter = new DefaultConverter();
			InputFileWrapper input = InputFileWrapper.valueOf(
					aptFile.getAbsolutePath(), from, "ISO-8859-1", converter
							.getInputFormats());

			
			OutputFileWrapper output = OutputFileWrapper.valueOf(out
					.getAbsolutePath(), to, "UTF-8", converter
					.getOutputFormats());

			converter.convert(input, output);
		} catch (UnsupportedFormatException e) {
			e.printStackTrace();
		} catch (ConverterException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream newHTML = fromAptToDocbook.performTransformation(new FileSystemResource(out));
		SAXParserFactory factory = SAXParserFactory.newInstance();

		// création d'un parseur SAX
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(new InputSource(newHTML), htmlParser);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}

		Book book = htmlParser.getBook();
		logger.debug("The book title is " + book.getTitle());
	}

	@Test
	public void testLatexConversion() {
		String result = /*
						 * generator .adaptContent(
						 */"<h2>SubTitle</h2><p>Lorem ipsum</p><h3>SubTitle2</h3><p>Lorem ipsum</p>"/* ) */;

		Resource resource = new ByteArrayResource(result.getBytes());

		/** Generate Latex */
		InputStream xhtml = toXHTMLConvertor.performTransformation(resource);
		InputStream improvedXhtml = toImprovedXHTML4LatexConvertor
				.performTransformation(new InputStreamResource(xhtml));
		InputStream latex = toLatexConvertor
				.performTransformation(new InputStreamResource(improvedXhtml));
		logger.debug("xhtml to apt ok");
		File latexFile;
		try {
			latexFile = File.createTempFile("wooki", ".latex");
			FileOutputStream fos = new FileOutputStream(latexFile);
			logger.debug("latex File is " + latexFile.getAbsolutePath());
			byte[] content = null;
			int available = 0;
			while ((available = latex.available()) > 0) {
				content = new byte[available];
				latex.read(content);
				fos.write(content);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddIds() {
		String result = generator
				.adaptContent(
						"<h2>SubTitle</h2><p>Lorem ipsum</p><h3>SubTitle2</h3><p>Lorem ipsum</p>",
						null);
		Assert
				.assertTrue(result
						.contains("<h2 id=\"b0\" class=\"commentable\">SubTitle</h2><p id=\"b1\" class=\"commentable\">Lorem ipsum</p><h3 id=\"b2\" class=\"commentable\">SubTitle2</h3><p id=\"b3\" class=\"commentable\">Lorem ipsum</p>"));
	}

	/**
	 * Create comments on chapter content and verify that when content changes,
	 * comments are well re-assigned.
	 */
	@Test
	public void testReAssignOnPreviousHeader() {

		String currentContent = "<chapter idStart='4'><h2 id='0'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='2'>SubTitle2</h3><p id='3'>Lorem ipsum</p></chapter>";
		String newContent = "<chapter idStart='5'><h2 id='0'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='2'>SubTitle2</h3><p id='4'>Lorem ipsum</p></chapter>";

		Comment comment = new Comment();
		comment.setContent("Too Long :)");
		comment.setDomId("3");

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);

		generator.reAssignComment(comments, currentContent, newContent);

		Assert.assertEquals(comment.getDomId(), "2",
				"Comment must be assigned to previous 'h' element id");
	}

	@Test
	public void testReAssignOnPreviousHeader2() {

		String currentContent = "<chapter idStart='4'><h2 id='0'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='2'>SubTitle2</h3><p id='3'>Lorem ipsum</p></chapter>";
		String newContent = "<chapter idStart='6'><h2 id='0'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='4'>SubTitle2</h3><p id='5'>Lorem ipsum</p></chapter>";

		Comment comment = new Comment();
		comment.setContent("Too Long :)");
		comment.setDomId("3");

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);

		generator.reAssignComment(comments, currentContent, newContent);

		Assert.assertEquals(comment.getDomId(), "0",
				"Comment must be assigned to previous 'h' element id");
	}

	@Test
	public void testReAssignOnBook() {

		String currentContent = "<chapter idStart='4'><h2 id='0'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='2'>SubTitle2</h3><p id='3'>Lorem ipsum</p></chapter>";
		String newContent = "<chapter idStart='5'><h2 id='4'>SubTitle</h2><p id='1'>Lorem ipsum</p><h3 id='2'>SubTitle2</h3><p id='3'>Lorem ipsum</p></chapter>";

		Comment comment = new Comment();
		comment.setContent("Too Long :)");
		comment.setDomId("0");

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);

		generator.reAssignComment(comments, currentContent, newContent);

		Assert.assertEquals(comment.getDomId(), null,
				"Comment must be assigned to previous 'h' element id");
	}

	@Test
	public void testReAssignOnParent() {

		String currentContent = "<chapter idStart='2'><p id='0'><p id='1'>SubContent</p></p></chapter>";
		String newContent = "<chapter idStart='3'><p id='0'><p id='2'>New SubContent</p></p></chapter>";

		Comment comment = new Comment();
		comment.setContent("Too Long :)");
		comment.setDomId("1");

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);

		generator.reAssignComment(comments, currentContent, newContent);

		Assert.assertEquals(comment.getDomId(), "0",
				"Comment must be assigned to previous 'h' element id");
	}

	@Test
	public void testAssignUnchanged() {

		String currentContent = "<chapter idStart='2'><p id='0'><p id='1'>SubContent</p></p></chapter>";
		String newContent = "<chapter idStart='2'><p id='0'><p id='1'>SubContent</p></p></chapter>";

		Comment comment = new Comment();
		comment.setContent("Too Long :)");
		comment.setDomId("1");

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(comment);

		generator.reAssignComment(comments, currentContent, newContent);

		Assert.assertEquals(comment.getDomId(), "1",
				"Comment must be assigned to previous 'h' element id");
	}

}
