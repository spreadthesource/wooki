package com.wooki.services;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
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
	public Comment addComment(Chapter chapter, User author, String content,
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
		comment.setUser(author);
		comment.setContent(content);
		comment.setChapter(chapter);
		toUpdate.addComment(comment);

		return comment;
	}

	public Chapter findById(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException("Chapter id cannot be null.");
		}
		return this.chapterDao.findById(chapterId);
	}

	public String getContent(Long chapterId) {
		Chapter chapter = chapterDao.findById(chapterId);
		if (chapter == null) {
			return null;
		}
		return chapterDao.getContent(chapter);
	}

	@Transactional(readOnly = false)
	public void updateContent(Long chapterId, String content) {
		Chapter chapter = chapterDao.findById(chapterId);
		if (chapter != null) {
			chapter.setContent(content.getBytes());
			chapter.setLastModified(Calendar.getInstance().getTime());
			chapterDao.update(chapter);
		}
	}

	@Transactional(readOnly = false)
	public void publishChapter(Long chapterId) {
		Chapter chapter = chapterDao.findById(chapterId);
		if (chapter == null) {
			throw new IllegalArgumentException(
					"Chapter parameter cannot be null for publication.");
		}
		if (chapter != null) {
			Publication published = new Publication();
			published.setChapter(chapter);
			try {
				published.setContent(domManager.adaptContent(
						new String(chapter.getContent())).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			published.setRevisionDate(Calendar.getInstance().getTime());
			publicationDao.create(published);
		}
	}

	public String getLastPublishedContent(Long chapterId) {
		if (chapterId == null) {
			throw new IllegalArgumentException();
		}
		Publication published = publicationDao.findLastRevision(chapterId);
		if (published != null && published.getContent() != null) {
			try {
				return new String(published.getContent(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
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

	public List<Chapter> listChaptersInfo(Long bookId) {
		return chapterDao.listChapterInfo(bookId);
	}

}
