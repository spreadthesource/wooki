package com.wooki.components.activity;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.domain.model.activity.CommentActivity;

/**
 * Display activities.
 * 
 * @author ccordenier
 * 
 */
public class Comment {

	@Property
	@Parameter(allowNull = false, required = true)
	private CommentActivity activity;

	public Object[] getChapterCtx() {
		return new Object[] {
				activity.getComment().getPublication().getChapter().getId(),
				activity.getComment().getPublication().getChapter().getBook()
						.getId() };
	}

}
