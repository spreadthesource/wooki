package com.wooki.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.User;
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
