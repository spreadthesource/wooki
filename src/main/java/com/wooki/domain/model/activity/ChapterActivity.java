package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.wooki.domain.model.Chapter;

/**
 * Log activities on book.
 * 
 * @author ccordenier
 * 
 */
@Entity
@PrimaryKeyJoinColumn(name = "CHAPTER_ACTIVITY_ID")
public class ChapterActivity extends Activity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Chapter chapter;

	private ChapterEventType type;

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setType(ChapterEventType type) {
		this.type = type;
	}

	public ChapterEventType getType() {
		return type;
	}

}
