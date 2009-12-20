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

package com.wooki.domain.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Chapter;

@Repository("chapterDao")
public class ChapterDAOImpl extends GenericDAOImpl<Chapter, Long> implements
		ChapterDAO {

	public List<Chapter> listChapterInfo(Long bookId) {
		if (bookId == null) {
			throw new IllegalArgumentException(
					"Book id should not be null while lis chapters informations");
		}
		Query query = entityManager
				.createQuery(String
						.format(
								"select NEW %s(c.id, c.title, c.lastModified) from %s c where c.book.id=:book and c.deletionDate is null",
								getEntityType(), getEntityType()));
		query.setParameter("book", bookId);
		return query.getResultList();
	}

	public List<Chapter> listChapters(Long idBook) {
		if (idBook == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " c where c.book.id=:book and c.deletionDate is null");
		List<Chapter> result = (List<Chapter>) query.setParameter("book",
				idBook).getResultList();
		return result;
	}

	public List<Chapter> listLastModified(Long id, int nbElts) {
		if (id == null) {
			throw new IllegalArgumentException("Book id cannot.");
		}
		Query query = this.entityManager
				.createQuery("from "
						+ this.getEntityType()
						+ " c where c.book.id=:booId and c.deletionDate is null order by c.lastModified desc");
		query.setParameter("bookId", id);
		return query.getResultList();
	}

}
