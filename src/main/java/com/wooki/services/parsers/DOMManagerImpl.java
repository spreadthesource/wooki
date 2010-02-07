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

package com.wooki.services.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tools.ant.filters.StringInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooki.domain.model.Comment;

public class DOMManagerImpl implements DOMManager {

	private static final String COMMENTABLE_CLASS = "commentable";

	private static final String BLOCKQUOTE = "blockquote";

	private final Set<String> COMMENTABLE = CollectionFactory.newSet("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "blockquote");

	private static final String CHAPTER_ROOT_NODE = "chapter";

	/**
	 * Used to set the current id to start from when assigning ids to node.
	 * 
	 */
	private static final String ID_START = "idStart";

	private final Logger logger = LoggerFactory.getLogger(DOMManagerImpl.class);

	private String characterEncoding = "UTF-8";

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

	public String adaptContent(String content, Long prefix) {
		StringBuffer result = new StringBuffer();
		result.append("<").append(CHAPTER_ROOT_NODE).append(" ").append(ID_START).append("=\"0\">");
		result.append(content);
		result.append("</").append(CHAPTER_ROOT_NODE).append(">");
		return addIds(result.toString(), prefix);
	}

	private String addIds(String document, Long prefix) {

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
			buildIds(allocator, elt, prefix);
		}

		// Set the new value for
		doc.getRootElement().setAttribute(ID_START, "" + allocator.current());
		return serializeContent(doc);

	}

	public void reAssignComment(List<Comment> comments, String content, String newContent) {

		Document currentDoc = parseContent(content);
		Document newDoc = parseContent(newContent);

		if (currentDoc != null && newDoc != null) {

			for (Comment comment : comments) {

				String domId = comment.getDomId();
				try {
					// Verify that the comment do no exist in the new document
					XPath path = XPath.newInstance("//*[@id=" + domId + "]");
					Element elt = (Element) path.selectSingleNode(newDoc.getRootElement());

					// Reassign if needed
					if (elt == null) {

						elt = (Element) path.selectSingleNode(currentDoc.getRootElement());

						// Cannot find the element in the existing document
						if (elt == null) {
							comment.setDomId(null);
							break;
						}

						if (elt.getParent() != null) {

							int idx = elt.getParent().indexOf(elt);
							boolean found = false;

							for (int i = idx - 1; i >= 0; i--) {
								Element cont = (Element) elt.getParentElement().getContent(i);
								if (cont.getName().startsWith("h")) {
									XPath reaffect = XPath.newInstance("//*[@id=" + cont.getAttributeValue("id") + "]");
									// Check that the new node still exist
									if (reaffect.selectSingleNode(newDoc) != null) {
										found = true;
										comment.setDomId(cont.getAttributeValue("id"));
										break;
									}
								}
							}

							if (!found) {
								if (CHAPTER_ROOT_NODE.equals(elt.getParentElement().getName())) {
									comment.setDomId(null);
								} else {
									comment.setDomId(elt.getParentElement().getAttributeValue("id"));
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
			logger.error("Document content cannot be parsed during comment reassignment.");
		}

	}

	/**
	 * Recursivily add ids to dom elements.
	 * 
	 * @param elt
	 */
	private void buildIds(IdAllocator allocator, Element elt, Long prefix) {
		if (COMMENTABLE.contains(elt.getName().toLowerCase())) {
			if (prefix != null) {
				elt.setAttribute("id", "b" + prefix.toString() + allocator.next());
			} else {
				elt.setAttribute("id", "b" + allocator.next());
			}
			elt.setAttribute("class", COMMENTABLE_CLASS);
		}

		if (!BLOCKQUOTE.equalsIgnoreCase(elt.getName())) {
			for (Element child : (List<Element>) elt.getChildren()) {
				buildIds(allocator, child, prefix);
			}
		} else {
			for (Element child : (List<Element>) elt.getChildren()) {
				clearId(child);
			}
		}
	}

	/**
	 * Clear all subsequent ids.
	 * 
	 * @param elt
	 */
	private void clearId(Element elt) {
		if (elt != null) {
			elt.removeAttribute("id");
			for (Element child : (List<Element>) elt.getChildren()) {
				clearId(child);
			}
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
			Document doc = builder.build(new StringInputStream(new String(content.getBytes(getCharacterEncoding()))));
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

		XMLOutputter output = new XMLOutputter();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			if (doc != null) {
				List<Element> children = doc.getRootElement().getChildren();
				if (children != null) {
					for (Element elt : children) {
						output.output(elt, bos);
					}
				}
			}
			bos.flush();
			return new String(bos.toByteArray(), getCharacterEncoding());
		} catch (IOException ioEx) {
			logger.error("Error during document serialization", ioEx);
			return "";
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException ioEx) {
					// Cannot do anything
				}
			}
		}
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

}
