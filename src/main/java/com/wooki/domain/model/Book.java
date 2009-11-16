package com.wooki.domain.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a book with its relation to other elements.
 * 
 * @author ccordenier
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "com.wooki.domain.model.book.findBySlugTitle", query = "select b from Book b where b.slugTitle=:st"),
		@NamedQuery(name = "com.wooki.domain.model.book.verifyBookOwner", query = "select b from Book b join b.authors as a where b.id=:id and a.username=:un") })
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_book", unique = true, nullable = false)
	private Long id;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "BookAuthor", joinColumns = @JoinColumn(name = "id_book"), inverseJoinColumns = { @JoinColumn(name = "surname") })
	private List<Author> authors;

	/**
	 * First element will always be the book abstract
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
	private List<Chapter> chapters;

	private String title;

	@Column(unique = true)
	private String slugTitle;

	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.DATE)
	private Date lastModified;

	public void addAuthor(Author author) {
		if (this.authors == null) {
			this.authors = new ArrayList<Author>();
		}
		authors.add(author);
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

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
