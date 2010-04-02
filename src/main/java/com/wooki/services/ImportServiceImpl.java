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

package com.wooki.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.services.parsers.Convertor;

public class ImportServiceImpl implements ImportService {

	@Autowired
	private HTMLParser handler;

	@Autowired
	private BookManager bookManager;

	private ChapterManager chapterManager;

	private Convertor toHTMLConvertor;

	private Convertor fromAptToDocbook;

	public Convertor getFromAptToDocbook() {
		return fromAptToDocbook;
	}

	public void setFromAptToDocbook(Convertor fromAptToDocbook) {
		this.fromAptToDocbook = fromAptToDocbook;
	}

	private Logger logger = Logger.getLogger(ImportServiceImpl.class);

	public Book importDocbook(Resource docbook) {
		/** Generate HTML */
		InputStream xhtml = toHTMLConvertor.performTransformation(docbook);
		return importDocbook(xhtml);
	}

	public Book importBook(Resource book) {
		Book toReturn = new Book();
		Converter converter = new DefaultConverter();
		File tempFile = null;
		try {
			InputFileWrapper input = InputFileWrapper.valueOf(book.getFile()
					.getAbsolutePath(), null, "ISO-8859-1", converter
					.getInputFormats());
			tempFile = File.createTempFile("DoxiaWooki", ".xhtml");
			OutputFileWrapper output = OutputFileWrapper.valueOf(tempFile
					.getAbsolutePath(), "xhtml", "UTF-8", converter
					.getOutputFormats());
			converter.convert(input, output);
		} catch (UnsupportedFormatException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription()
					+ " because of an unsupported format");
			logger.error(e.getLocalizedMessage());
		} catch (ConverterException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription()
					+ " because of a Doxia Converter exception");
			logger.error(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription()
					+ " because of an IllegalArgument exception");
			logger.error(e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription() + " because of the encoding");
			logger.error(e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription()
					+ " because of a FileNotFound exception");
		} catch (IOException e) {
			logger.error("Didn't manage to import the book contained in "
					+ book.getDescription() + " because of an IO exception");
			logger.error(e.getLocalizedMessage());
			return null;
		}

		InputStream correctedXHTML = fromAptToDocbook
				.performTransformation(new FileSystemResource(tempFile));
		return importDocbook(correctedXHTML);
	}

	public Book importDocbook(InputStream generatedXhtml) {

		handler.setEntityResolver((EntityResolver) toHTMLConvertor);
		SAXParserFactory factory = SAXParserFactory.newInstance();

		// création d'un parseur SAX
		SAXParser parser;

		try {
			parser = factory.newSAXParser();
			parser.parse(new InputSource(generatedXhtml), handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		}

		Book book = handler.getBook();
		return book;
	}

	public BookManager getBookManager() {
		return bookManager;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}

	public ChapterManager getChapterManager() {
		return chapterManager;
	}

	public void setChapterManager(ChapterManager chapterManager) {
		this.chapterManager = chapterManager;
	}

	public Convertor getToHTMLConvertor() {
		return toHTMLConvertor;
	}

	public void setToHTMLConvertor(Convertor toHTMLConvertor) {
		this.toHTMLConvertor = toHTMLConvertor;
	}

	public Book importApt(Resource apt) {
		return importBook(apt);
	}
}
