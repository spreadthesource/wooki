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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Represents a book with its relation to other elements.
 */
@Entity
public class Book extends WookiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_book", unique = true, nullable = false)
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "BookAuthor", joinColumns = @JoinColumn(name = "id_book"), inverseJoinColumns = { @JoinColumn(name = "id_user") })
	private List<User> users;

	/**
	 * First element will always be the book abstract
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
	private List<Chapter> chapters;

	private String title;

	@Column(unique = true)
	private String slugTitle;

	public void addUser(User user) {
		if (this.users == null) {
			this.users = new ArrayList<User>();
		}
		users.add(user);
	}

	public void addChapter(Chapter chapter) {
		if (this.chapters == null) {
			this.chapters = new ArrayList<Chapter>();
		}
		chapters.add(chapter);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getAuthors() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Chapter> getChapters() {
		if (chapters == null) {
			this.chapters = new ArrayList<Chapter>();
		}
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlugTitle() {
		return slugTitle;
	}

	public void setSlugTitle(String titleSlug) {
		this.slugTitle = titleSlug;
	}

}
