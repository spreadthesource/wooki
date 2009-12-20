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

import com.wooki.domain.model.Book;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.domain.model.activity.CommentActivity;

@Repository("activityDao")
public class ActivityDAOImpl extends GenericDAOImpl<Activity, Long> implements
		ActivityDAO {

	public List<Activity> list(int nbElements) {
		Query query = entityManager
				.createQuery("from "
						+ getEntityType()
						+ " a where a.deletionDate is null order by a.creationDate desc");
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listActivityOnUserBooks(int nbElts, Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"User id cannot be null to find its activities");
		}
		Query query = entityManager
				.createQuery("select distinct a from "
						+ Activity.class.getName()
						+ " a, "
						+ ChapterActivity.class.getName()
						+ " ca, "
						+ CommentActivity.class.getName()
						+ " coa, "
						+ BookActivity.class.getName()
						+ " ba, "
						+ Book.class.getName()
						+ " b join b.users u where ( (b.id=coa.comment.publication.chapter.book.id and coa.id=a.id) or (b.id=ca.chapter.book.id and ca.id=a.id) or (b.id=ba.book.id and ba.id=a.id)) and u.id=:uid and a.user.id!=:uid order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElts);
		return query.getResultList();
	}

	public List<Activity> listUserActivity(int nbElts, Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"User id cannot be null to find its activities");
		}
		Query query = entityManager
				.createQuery("select a from "
						+ getEntityType()
						+ " a where a.deletionDate is null and a.user.id=:uid order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElts);
		return query.getResultList();
	}

	public List<Activity> listActivityOnBook(int nbElements, Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException(
					"User id cannot be null to find its activities");
		}
		Query query = entityManager
				.createQuery("select distinct a from "
						+ Activity.class.getName()
						+ " a, "
						+ ChapterActivity.class.getName()
						+ " ca, "
						+ CommentActivity.class.getName()
						+ " coa, "
						+ BookActivity.class.getName()
						+ " ba, "
						+ Book.class.getName()
						+ " b join b.users u where ( (b.id=coa.comment.publication.chapter.book.id and coa.id=a.id) or (b.id=ca.chapter.book.id and ca.id=a.id) or (b.id=ba.book.id and ba.id=a.id)) and u.id=:uid and a.user.id=:uid order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listBookCreationActivity(int nbElements) {
		Query query = entityManager
				.createQuery("from "
						+ BookActivity.class.getName()
						+ " a where a.deletionDate is null and a.type=:type order by a.creationDate desc");
		query.setParameter("type", BookEventType.CREATE);
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

}
