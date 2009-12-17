package com.wooki.domain.dao;

import com.wooki.domain.model.Publication;

/**
 * Publication will be used to follow book chapter revision.
 *
 * @author ccordenier
 *
 */
public interface PublicationDAO extends GenericDAO<Publication, Long>{

	/**
	 * Used to retrieve the last revision of a chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	Publication findLastRevision(Long chapterId);
	
	/**
	 * Used to retrieve the last published revision of a chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	Publication findLastPublishedRevision(Long chapterId);
	
}
