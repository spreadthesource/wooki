package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.wooki.domain.model.Comment;

@Entity
@PrimaryKeyJoinColumn(name = "COMMENT_ACTIVITY_ID")
public class CommentActivity extends Activity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Comment comment;

	private CommentEventType type;

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public CommentEventType getType() {
		return type;
	}

	public void setType(CommentEventType type) {
		this.type = type;
	}

}
