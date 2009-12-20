package com.wooki.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * User added comment on existing book.
 *
 * @author ccordenier
 * 
 */
@Entity
public class Comment extends WookiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private String title;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Publication publication;

	@OneToOne
	private CommentLabel label;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CommentState state;

	@Column(nullable = false)
	private String domId;

	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication chapter) {
		this.publication = chapter;
	}

	public CommentLabel getLabel() {
		return label;
	}

	public void setLabel(CommentLabel state) {
		this.label = state;
	}

	public String getDomId() {
		return domId;
	}

	public void setDomId(String domId) {
		this.domId = domId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CommentState getState() {
		return state;
	}

	public void setState(CommentState state) {
		this.state = state;
	}

}
