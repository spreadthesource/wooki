package com.wooki.domain.dao;

import com.wooki.domain.model.Publication;

public interface PublicationDAO extends GenericDAO<Publication, Long>{

	/**
	 * Used to retrieve the last revision of a chapter.
	 *
	 * @param chapterId
	 * @return
	 */
	Publication findLastRevision(Long chapterId);
	
}
