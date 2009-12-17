package com.wooki.domain.biz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.security.WookiSecurityContext;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("chapterManager")
public class ChapterManagerImpl extends AbstractManager implements ChapterManager {

	@Autowired
	private ChapterDAO chapterDao;

	@Autowired
	private PublicationDAO publicationDao;

	@Autowired
	// TODO : to delete?
	private DOMManager domManager;

	@Autowired
	private WookiSecurityContext securityCtx;

	@Transactional(readOnly = false)
	public Comment addComment(Long publicationId, String content, String domId) {

		// Check security
		if (!securityCtx.isLoggedIn()) {
			throw new AuthorizationException("You must be logged in to add a comment.");
		}
		User author = securityCtx.getAuthor();

		if (publicationId == null || content == null) {
			throw new IllegalArgumentException("Chapter and comment cannot be null for addition.");
		}

		Publication toUpdate = publicationDao.findById(publicationId);
		Comment comment = new Comment();
		comment.setState(CommentState.OPEN);
		comment.setCreationDate(new Date());
		comment.setDomId(domId);
		comment.setUser(author);
		comment.setContent(content);
		comment.setPublication(toUpdate);
		toUpdate.addComment(comment);

		return comment;
	}

	public Chapter findById(Long chapterId) {
		return this.chapterDao.findById(chapterId);
	}

	public Publication getLastPublication(Long chapterId) {
		protectionNotNull(chapterId);

		return publicationDao.findLastRevision(chapterId);
	}

	public String getLastContent(Long chapterId) {
		Publication publication = getLastPublication(chapterId);

		if (publication != null && publication.getContent() != null) {
			return toStringWithCharset(publication.getContent(), "UTF-8");
		}

		return null;
	}

	public Publication getLastPublishedPublication(Long chapterId) {
		protectionNotNull(chapterId);

		return publicationDao.findLastPublishedRevision(chapterId);
	}

	public String getLastPublishedContent(Long chapterId) {
		Publication published = getLastPublishedPublication(chapterId);

		if (published != null && published.getContent() != null) {
			return toStringWithCharset(published.getContent(), "UTF-8");
		}

		return null;
	}

	@Transactional(readOnly = false)
	public void publishChapter(Long chapterId) {
		protectionNotNull(chapterId);

		Publication published = publicationDao.findLastRevision(chapterId);

		published.setLastModified(Calendar.getInstance().getTime());
		published.setPublished(true);

		publicationDao.update(published);
	}

	@Transactional(readOnly = false)
	public void updateContent(Long chapterId, String content) {
		protectionNotNull(chapterId);

		Publication publication = publicationDao.findLastRevision(chapterId);

		// we check the published flag. If set, then this Publication must
		// be considered as "locked" and we must create a new publication as
		// the new working copy
		if (publication == null || (publication != null && publication.isPublished())) {
			publication = new Publication();

			Chapter chapter = chapterDao.findById(chapterId);
			publication.setChapter(chapter);

			publication.setCreationDate(Calendar.getInstance().getTime());
			publicationDao.create(publication);
		}

		publication.setContent(content.getBytes());
		publication.setLastModified(Calendar.getInstance().getTime());
		
		publicationDao.update(publication);

	}

	@Transactional(readOnly = false)
	public void updateAndPublishContent(Long chapterId, String content) {
		updateContent(chapterId, content);
		publishChapter(chapterId);
	}

	@Transactional(readOnly = false)
	public void delete(Chapter chapter) {
		protectionNotNull(chapter);

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
