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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

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

	@ManyToOne(fetch = FetchType.EAGER)
	private User owner;

	/**
	 * First element will always be the book abstract
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_book", nullable = false)
	@IndexColumn(name = "CHAPTER_POSITION")
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}
