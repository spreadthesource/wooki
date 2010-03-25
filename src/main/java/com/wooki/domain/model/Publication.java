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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * This class is used to publish chapter when an author has decided to publish its work after
 * comment validation.
 */
@Entity
public class Publication extends WookiEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chapter", nullable = false)
    private Chapter chapter;

    @Lob
    private String content;

    private boolean published;

    /** The list of comment associated to the current publication */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publication", fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Publication()
    {

    }

    public Publication(Long id, boolean published)
    {
        this.id = id;
        this.published = published;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Chapter getChapter()
    {
        return chapter;
    }

    public void setChapter(Chapter chapter)
    {
        this.chapter = chapter;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void addComment(Comment com)
    {
        if (this.comments == null)
        {
            this.comments = new ArrayList<Comment>();
        }
        this.comments.add(com);
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public void setPublished(boolean published)
    {
        this.published = published;
    }

    public boolean isPublished()
    {
        return published;
    }

}
