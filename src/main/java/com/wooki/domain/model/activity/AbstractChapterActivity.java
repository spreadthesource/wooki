package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.wooki.domain.model.Chapter;

@Entity
@Table(name = "AbstractChaptersActivities")
@PrimaryKeyJoinColumn(name = "abstract_chapter_activity_id")
public class AbstractChapterActivity extends AbstractBookActivity
{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    public Chapter getChapter()
    {
        return chapter;
    }

    public void setChapter(Chapter chapter)
    {
        this.chapter = chapter;
    }

}
