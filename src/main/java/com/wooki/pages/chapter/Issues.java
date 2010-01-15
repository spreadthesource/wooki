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

package com.wooki.pages.chapter;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.base.BookBase;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.services.HttpError;

/**
 * Display all the comment for a given chapter.
 * 
 * @author ccordenier
 * 
 */
public class Issues extends BookBase {

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private CommentManager commentManager;

	@Property
	private Chapter chapter;

	@Property
	private List<Comment> comments;

	@Property
	private Comment current;
	
	@Property
	private int loopIdx;
	
	private Long chapterId;

	@OnEvent(value = EventConstants.ACTIVATE)
	public Object setupChapter(Long bookId, Long chapterId) {

		// Get book related information
		this.chapterId = chapterId;
		chapter = this.chapterManager.findById(chapterId);
		if (chapter == null) {
			return new HttpError(404, "Chapter not found");
		}

		this.comments = this.commentManager.listForChapter(this.chapterId);
		
		return null;
	}

	/**
	 * Prepare display of all the comments.
	 * 
	 */
	public void setupCommentDisplay() {
		this.comments = this.commentManager.listForChapter(chapterId);
	}

	public Object[] getChapterCtx() {
		return new Object[] { this.getBookId(), this.chapterId };
	}

	public Object[] getRevisionCtx() {
		return new Object[] { this.getBookId(), this.chapterId, this.current.getPublication().getId() };
	}

	public String getStyle() {
		return this.loopIdx == 0 ? "first" : null;
	}
	
	@OnEvent(value = EventConstants.PASSIVATE)
	public Object[] retrieveBookId() {
		return new Object[] { this.getBookId(), this.chapterId };
	}

}
