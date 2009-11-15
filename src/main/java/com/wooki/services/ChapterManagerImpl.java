package com.wooki.services;

import java.util.Date;
import java.util.List;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.model.Author;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.services.parsers.DOMManager;

public class ChapterManagerImpl implements ChapterManager {

	private ChapterDAO chapterDao;

	private BookDAO bookDao;

	private ActivityDAO activityDao;

	private DOMManager domManager;

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
		chapterDao.update(toUpdate);

		return comment;
	}

	public String getContent(Chapter chapter) {
		return chapterDao.getContent(chapter);
	}

	public void updateContent(Chapter chapter, String content) {

		List<Comment> comments = chapter.getComments();

		// First call to updateContent = content creation
		if (chapter.getContent() == null) {
			chapter.setContent(domManager.adaptContent(content));
			chapterDao.update(chapter);
			return;
		}

		domManager.reAssignComment(comments, chapter.getContent(), content);

		// Get the book abstract
		chapter.getBook().getChapters();
		Chapter bookAbstract = chapter.getBook().getChapters().get(0);

		for (Comment comment : comments) {
			// Remove and Re-assign to book abstract
			if (comment.getDomId() == null) {
				chapter.getComments().remove(comment);
				if (bookAbstract != null) {
					bookAbstract.addComment(comment);
				}
			}
		}

		bookDao.update(chapter.getBook());

		chapter.setLastModifed(new Date(System.currentTimeMillis()));
		chapterDao.update(chapter);
	}

	public void delete(Chapter chapter) {
		if (chapter == null) {
			throw new IllegalArgumentException(
					"Book and chapter cannot be null");
		}
		chapterDao.delete(chapter);
	}

	public List<Chapter> listChapters(Long bookId) {
		return chapterDao.listChapters(bookId);
	}

	public void setChapterDao(ChapterDAO chapterDao) {
		this.chapterDao = chapterDao;
	}

	public void setActivityDao(ActivityDAO activityDao) {
		this.activityDao = activityDao;
	}

	public void setBookDao(BookDAO bookDao) {
		this.bookDao = bookDao;
	}

	public void setDomManager(DOMManager domManager) {
		this.domManager = domManager;
	}

}
