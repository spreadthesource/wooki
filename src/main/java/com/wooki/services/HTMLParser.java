package com.wooki.services;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.exception.UserAlreadyOwnerException;
import com.wooki.domain.exception.UserNotFoundException;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.User;
import com.wooki.services.security.WookiSecurityContext;

public class HTMLParser extends DefaultHandler {
	private Book book = new Book();

	private EntityResolver entityResolver;

	@Autowired
	private UserManager userManager;
	@Autowired
	private ChapterManager chapterManager;
	@Autowired
	private BookManager bookManager;
	@Autowired
	private WookiSecurityContext securityCtx;

	private boolean isInBookTitle = false;
	private boolean isInAuthor;
	private String currentEmail;
	private boolean isInEmail;
	private String currentFirstName;
	private boolean isInFirstName;
	private String currentSurname;
	private boolean isInSurname;
	private String currentChapterTitle;
	private boolean isInChapter;
	private int divCount;

	private Logger logger = Logger.getLogger(HTMLParser.class);
	StringBuffer buffer = new StringBuffer();

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (!isInChapter && qName.equals("h1")
				&& attributes.getValue("class").equals("title")) {
			isInBookTitle = true;
		} else {
			if (!isInChapter && qName.equals("div")
					&& attributes.getValue("class") != null
					&& attributes.getValue("class").equals("author")) {
				isInAuthor = true;
			} else {
				if (!isInChapter && isInAuthor && qName.equals("span")
						&& attributes.getValue("class").equals("firstname")) {
					isInFirstName = true;
				} else {
					if (!isInChapter && isInAuthor && qName.equals("span")
							&& attributes.getValue("class").equals("surname")) {
						isInSurname = true;
					} else {
						if (!isInChapter && isInAuthor && qName.equals("code")
								&& attributes.getValue("class").equals("email")) {
							isInEmail = true;
						} else {
							if (qName.equals("div")
									&& attributes.getValue("class") != null
									&& attributes.getValue("class").equals(
											"chapter")) {
								isInChapter = true;
								divCount = 0;
								buffer.setLength(0);
								currentChapterTitle = attributes
										.getValue("title");
								if (currentChapterTitle == null) {
									currentChapterTitle = "No title";
								}
							} else {
								if (isInChapter) {
									buffer.append("<").append(qName);
									String currentName;
									int length = attributes.getLength();
									for (int i = 0; i < length; i++) {
										currentName = attributes.getQName(i);
										buffer.append(" ").append(currentName)
												.append("=\"").append(
														attributes.getValue(i))
												.append("\"");
									}
									buffer.append(">");
									if (qName.equals("div") && isInChapter) {
										divCount++;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("h1") && isInBookTitle) {
			book.setTitle(buffer.toString());
			buffer.setLength(0);
			isInBookTitle = false;
		} else {
			if (isInFirstName && qName.equals("span")) {
				currentFirstName = buffer.toString();
				isInFirstName = false;
				buffer.setLength(0);
			} else {
				if (isInSurname && qName.equals("span")) {
					currentSurname = buffer.toString();
					isInSurname = false;
					buffer.setLength(0);
				} else {
					if (isInEmail && qName.equals("code")) {
						currentEmail = buffer.toString();
						isInEmail = false;
						buffer.setLength(0);
					} else {
						if (qName.equals("div") && isInAuthor) {
							String authorName = currentFirstName + " "
									+ currentSurname;
							User user = userManager.findByUsername(authorName);
							// KOLOSSAL FAILLE !
							if (user == null) {
								user = new User();
								user.setFullname(authorName);
								user.setCreationDate(new Date());
								user.setEmail(currentEmail);
								user.setUsername(authorName);
								user.setPassword(currentFirstName);
								try {
									userManager.addUser(user);
									securityCtx.log(user);
								} catch (UserAlreadyException e) {
									logger.error(e.getLocalizedMessage());
								}
							}
							book = bookManager.create(book.getTitle());
							try {
								bookManager.addAuthor(book, authorName);
							} catch (UserNotFoundException e) {
								logger.error(e.getLocalizedMessage());
							} catch (UserAlreadyOwnerException e) {
								logger.error(e.getLocalizedMessage());
							}
							isInAuthor = false;
						} else {
							if (isInChapter) {
								buffer.append("</").append(qName).append(">");
								if (qName.equals("div")) {
									divCount--;
									if (divCount <= 0) {
										Chapter currentChapter = bookManager
												.addChapter(book,
														currentChapterTitle);
										chapterManager.updateContent(
												currentChapter.getId(), buffer
														.toString());
										buffer.setLength(0);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length);
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws IOException, SAXException {
		return getEntityResolver().resolveEntity(publicId, systemId);
	}

	public EntityResolver getEntityResolver() {
		return this.entityResolver;
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		this.entityResolver = entityResolver;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
