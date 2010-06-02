package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.wooki.domain.model.Chapter;

@Entity
public class AbtractChapterActivity extends AbstractBookActivity
{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
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
