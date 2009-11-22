package com.wooki.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.model.Author;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.domain.model.Publication;
import com.wooki.services.parsers.DOMManager;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("chapterManager")
public class ChapterManagerImpl implements ChapterManager {

	@Autowired
	private ChapterDAO chapterDao;

	@Autowired
	private PublicationDAO publicationDao;

	@Autowired
	private DOMManager domManager;

	@Transactional(readOnly = false)
	public Comment addComment(Chapter chapter, Author author, String content,
			String domId) {
		if (chapter == null || content == null) {
			throw new IllegalArgumentException(
					"Chapter and comment cannot be null for addition.");
		}

		Chapter toUpdate = chapterDao.findById(chapter.getId());
		Comment comment = new Comment();
		comment.setState(CommentState.OPEN);
		comment.setCreationDate(new Date());
		comment.setDomId(domId);
		comment.setAuthor(author);
		comment.setContent(content);
		comment.setChapter(chapter);
		toUpdate.addComment(comment);

		return comment;
	}

	public String getContent(Chapter chapter) {
		return chapterDao.getContent(chapter);
	}

	@Transactional(readOnly = false)
	public void updateContent(Chapter chapter, String content) {
		chapter.setContent(content);
		chapter.setLastModifed(new Date());
		chapterDao.update(chapter);
	}

	@Transactional(readOnly = false)
	public void publishChapter(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException("Chapter parameter cannot be null for publication.");
		}
		Chapter toPublish = chapterDao.findById(chapter.getId());
		if (chapter != null) {
			Publication published = new Publication();
			published.setChapter(chapter);
			published.setContent(domManager.adaptContent(chapter.getContent()));
			published.setRevisionDate(new Date());
			publicationDao.create(published);
		}
	}

	public Publication getLastPublishedContent(Long chapterId) {
		return publicationDao.findLastRevision(chapterId);
	}

	@Transactional(readOnly = false)
	public void delete(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException(
					"Book and chapter cannot be null");
		}
		Chapter toDelete = chapterDao.findById(chapter.getId());
		chapterDao.delete(toDelete);
	}

	public List<Chapter> listChapters(Long bookId) {
		return chapterDao.listChapters(bookId);
	}

}
