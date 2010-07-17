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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a chapter of the book.
 */
@Entity
@Table(name = "Chapters")
public class Chapter extends WookiEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "chapter_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, updatable = false, insertable = false)
    private Book book;

    /** User friendly title */
    @Column(name = "title")
    private String title;

    /** Identifier title */
    @Column(name = "slugTitle")
    private String slugTitle;

    /**
     * Constructor used to retrieve only required information for chapters display.
     * 
     * @param id
     * @param title
     * @param lastModified
     */
    public Chapter(long id, String title, Date lastModified)
    {
        this.id = id;
        this.title = title;
        this.setLastModified(lastModified);
    }

    public Chapter()
    {

    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSlugTitle()
    {
        return slugTitle;
    }

    public void setSlugTitle(String titleSlug)
    {
        this.slugTitle = titleSlug;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

}
