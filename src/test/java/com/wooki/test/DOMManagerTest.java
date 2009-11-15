package com.wooki.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.domain.model.Comment;
import com.wooki.services.parsers.Convertor;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.parsers.XHTMLToFormattingObjects;

/**
 * Test case for DOM manipulation.
 * 
 * @author ccordenier
 * 
 */
@ContextConfiguration(locations = { "/daoContext.xml",
		"/applicationContext.xml" })
public class DOMManagerTest extends AbstractTestNGSpringContextTests {

	private Logger logger = Logger.getLogger(DOMManagerTest.class);
	
	private DOMManager generator;
	private Convertor toFOConvertor;

	public Convertor getToFOConvertor() {
		return toFOConvertor;
	}

	public void setToFOConvertor(Convertor toFOConvertor) {
		this.toFOConvertor = toFOConvertor;
	}

	private Convertor toPDFConvertor;
	private Convertor toXHTMLConvertor;

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

	@BeforeClass
	public void init() {
		generator = (DOMManager) applicationContext.getBean("domManager");
		toFOConvertor = (Convertor) applicationContext
				.getBean("xhtmlToFOConvertor");
		toPDFConvertor = (Convertor) applicationContext
				.getBean("FOToPDFConvertor");
		toXHTMLConvertor = (Convertor) applicationContext
				.getBean("documentToXHTMLConvertor");
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
			pdfFile = File.createTempFile("toto", ".pdf");
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
	public void testAddIds() {
		String result = generator
				.adaptContent("<html><head></head><body><h2>SubTitle</h2><p>Lorem ipsum</p><h3>SubTitle2</h3><p>Lorem ipsum</p></body></html>");
		Assert.assertTrue(result.contains("idStart=\"4\""));
		Assert
				.assertTrue(result
						.contains("<h2 id=\"0\">SubTitle</h2><p id=\"1\">Lorem ipsum</p><h3 id=\"2\">SubTitle2</h3><p id=\"3\">Lorem ipsum</p>"));
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