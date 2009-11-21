package com.wooki.services.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.filters.StringInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.transform.JDOMSource;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wooki.domain.model.Comment;

@Service("domManager")
public class DOMManagerImpl implements DOMManager {

	private static final String CHAPTER_ROOT_NODE = "chapter";

	/**
	 * Used to set the current id to start from when assigning ids to node.
	 * 
	 */
	private static final String ID_START = "idStart";

	private final Logger logger = LoggerFactory.getLogger(DOMManagerImpl.class);

	/**
	 * Used to allocate id on content.
	 * 
	 * @author ccordenier
	 * 
	 */
	class IdAllocator {

		private long startIdx;

		public IdAllocator(long startIdx) {
			this.startIdx = startIdx;
		}

		public long next() {
			return startIdx++;
		}

		public long current() {
			return startIdx;
		}
	}

	public String adaptContent(String content) {
		StringBuffer result = new StringBuffer();
		result.append("<").append(CHAPTER_ROOT_NODE).append(" ")
				.append(ID_START).append("=\"0\">");
		result.append(content);
		result.append("</").append(CHAPTER_ROOT_NODE).append(">");
		return addIds(result.toString());
	}

	public String addIds(String document) {

		// Parse document
		Document doc = parseContent(document);

		if (doc == null) {
			return null;
		}

		// Get the current number of node used to add unique id on node to
		// link with comments
		String nbNode = doc.getRootElement().getAttributeValue(ID_START);
		long idx = Long.parseLong(nbNode);
		IdAllocator allocator = new IdAllocator(idx);

		for (Element elt : (List<Element>) doc.getRootElement().getChildren()) {
			buildIds(allocator, elt);
		}

		// Set the new value for
		doc.getRootElement().setAttribute(ID_START, "" + allocator.current());
		return serializeContent(doc);

	}

	public void reAssignComment(List<Comment> comments, String content,
			String newContent) {

		Document currentDoc = parseContent(content);
		Document newDoc = parseContent(newContent);

		if (currentDoc != null && newDoc != null) {

			for (Comment comment : comments) {

				String domId = comment.getDomId();
				try {
					// Verify that the comment do no exist in the new document
					XPath path = XPath.newInstance("//*[@id=" + domId + "]");
					Element elt = (Element) path.selectSingleNode(newDoc
							.getRootElement());

					// Reassign if needed
					if (elt == null) {

						elt = (Element) path.selectSingleNode(currentDoc
								.getRootElement());

						// Cannot find the element in the existing document
						if (elt == null) {
							comment.setDomId(null);
							break;
						}

						if (elt.getParent() != null) {

							int idx = elt.getParent().indexOf(elt);
							boolean found = false;

							for (int i = idx - 1; i >= 0; i--) {
								Element cont = (Element) elt.getParentElement()
										.getContent(i);
								if (cont.getName().startsWith("h")) {
									XPath reaffect = XPath
											.newInstance("//*[@id="
													+ cont
															.getAttributeValue("id")
													+ "]");
									// Check that the new node still exist
									if (reaffect.selectSingleNode(newDoc) != null) {
										found = true;
										comment.setDomId(cont
												.getAttributeValue("id"));
										break;
									}
								}
							}

							if (!found) {
								if (CHAPTER_ROOT_NODE.equals(elt
										.getParentElement().getName())) {
									comment.setDomId(null);
								} else {
									comment.setDomId(elt.getParentElement()
											.getAttributeValue("id"));
								}
							}

						} else {

						}
					}
				} catch (JDOMException e) {
					logger.error(e.getMessage());
				}

			}
		} else {
			logger
					.error("Document content cannot be parsed during comment reassignment.");
		}

	}

	/**
	 * Recursivily add ids to dom elements.
	 * 
	 * @param elt
	 */
	private void buildIds(IdAllocator allocator, Element elt) {
		if (elt.getAttribute("id") == null) {
			elt.setAttribute("id", "" + allocator.next());
		}
		for (Element child : (List<Element>) elt.getChildren()) {
			buildIds(allocator, child);
		}
	}

	/**
	 * Used to transform String into DOM element.
	 * 
	 * @param content
	 * @return
	 */
	private Document parseContent(String content) {
		try {
			SAXBuilder builder = new SAXBuilder();
			builder.setValidation(false);
			builder.setIgnoringElementContentWhitespace(true);
			Document doc = builder.build(new StringInputStream(content));
			return doc;
		} catch (JDOMException jdEx) {
			logger.error("Error during document parsing", jdEx);
		} catch (IOException ioEx) {
			logger.error("Error while reading parse document", ioEx);
		}

		// Return null in case of errros
		return null;
	}

	/**
	 * Transform DOM into String representation.
	 * 
	 * @param doc
	 * @return
	 */
	private String serializeContent(Document doc) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			// Serialize document
			Transformer transformer;

			transformer = TransformerFactory.newInstance().newTransformer();
			JDOMSource source = new JDOMSource(doc);
			StreamResult result = new StreamResult(bos);
			transformer.transform(source, result);

			return new String(bos.toByteArray());

		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			logger.error(e.getMessage());
		} catch (TransformerException e) {
			logger.error(e.getMessage());
		} finally {

			// Close output stream
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return null;
	}

}
