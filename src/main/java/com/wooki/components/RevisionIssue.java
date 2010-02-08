package com.wooki.components;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.model.Comment;
import com.wooki.services.utils.DateUtils;

/**
 * Display comments and revision for a given chapter.
 * 
 * @author ccordenier
 * 
 */
public class RevisionIssue {

	@Inject
	private CommentManager commentManager;

	@Parameter(required = true, allowNull = false)
	private Long bookId;
	
	@Parameter(required = true, allowNull = false)
	private Long chapterId;
	
	@Parameter
	@Property
	private boolean abstractChapter;
	
	@Property
	private List<Comment> comments;

	@Property
	private Comment current;

	@Property
	private int loopIdx;
	
	@Property
	private SimpleDateFormat format = DateUtils.getLastModified(); 
	
	@SetupRender
	public void listComments() {
		this.comments = this.commentManager.listForChapter(this.chapterId);
	}

	public Object[] getRevisionCtx() {
		return new Object[] { this.bookId, this.chapterId, this.current.getPublication().getId() };
	}

	public Object[] getPublishedCtx() {
		return new Object[] { this.bookId, this.chapterId };
	}

	public Object[] getAbstractRevisionCtx() {
		return new Object[] { this.bookId, this.current.getPublication().getId() };
	}

	public Object[] getAbstractPublishedCtx() {
		return new Object[] { this.bookId };
	}
	
	public String getStyle() {
		return this.loopIdx == 0 ? "first" : null;
	}

}
