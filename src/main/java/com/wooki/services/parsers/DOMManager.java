package com.wooki.services.parsers;

import java.util.List;

import com.wooki.domain.model.Comment;



/**
 * Used to handle DOM manipulation for Wooki application. 
 *
 * @author ccordenier
 *
 */
public interface DOMManager {

	/**
	 * Will suround all the content and generate ids.
	 *
	 * @param content
	 * @return
	 */
	public String adaptContent(String content);

	/**
	 * Used to re-assign comment ids.
	 *
	 * @param comments
	 * @param content
	 * @param newContent
	 */
	void reAssignComment(List<Comment> comments, String content, String newContent);

}
