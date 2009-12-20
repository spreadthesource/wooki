package com.wooki.components.activity;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;

/**
 * Display activities.
 * 
 * @author ccordenier
 * 
 */
public class Chapter {

	@Property
	@Parameter(allowNull = false, required = true)
	private ChapterActivity activity;

	public Object[] getChapterCtx() {
		return new Object[] { activity.getChapter().getBook().getId(),
				activity.getChapter().getId() };
	}

	public boolean isDisplayLink() {
		return ChapterEventType.PUBLISHED.equals(activity.getType());
	}
}
