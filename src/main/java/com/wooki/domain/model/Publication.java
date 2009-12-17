package com.wooki.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * This class is used to publish chapter when an author has decided to publish
 * its work after comment validation.
 */
@Entity
public class Publication extends WookiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	private Chapter chapter;

	@Lob
	private byte[] content;

	private boolean published;

	/** The list of comment associated to the current publication */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "publication", fetch = FetchType.LAZY)
	private List<Comment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void addComment(Comment com) {
		if (this.comments == null) {
			this.comments = new ArrayList<Comment>();
		}
		this.comments.add(com);
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isPublished() {
		return published;
	}
}
