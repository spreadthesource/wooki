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

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;

@Repository("activityDao")
public class ActivityDAOImpl extends GenericDAOImpl<Activity, Long> implements ActivityDAO {

	public List<Activity> list(int nbElements) {
		Query query = entityManager.createQuery("from " + getEntityType() + " a where a.deletionDate is null order by a.creationDate desc");
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listAllActivitiesOnComment(Long commentId) {
		Defense.notNull(commentId, "commentId");
		Query query = entityManager.createQuery("select ca from " + CommentActivity.class.getName() + " ca where ca.id=:cid");
		query.setParameter("cid", commentId);
		return query.getResultList();
	}
	
	public List<Activity> listAllActivitiesOnChapter(Long chapterId) {
		Defense.notNull(chapterId, "chapterId");
		Query query = entityManager.createQuery("select a from " + Activity.class.getName() + " a where a.id in (select id from " + CommentActivity.class.getName()
				+ " coa where coa.comment.publication.chapter.book.id=:cid) or a.id in (select id from " + ChapterActivity.class.getName()
				+ " ca where ca.chapter.book.id=:cid) ");
		query.setParameter("cid", chapterId);
		return query.getResultList();
	}
	
	public List<Activity> listAllActivitiesOnBook(Long bookId) {
		Defense.notNull(bookId, "bookId");
		Query query = entityManager.createQuery("select a from " + Activity.class.getName() + " a where a.id in (select id from "
				+ BookActivity.class.getName() + " ba where ba.book.id=:bid) or a.id in (select id from " + CommentActivity.class.getName()
				+ " coa where coa.comment.publication.chapter.book.id=:bid) or a.id in (select id from " + ChapterActivity.class.getName()
				+ " ca where ca.chapter.book.id=:bid) ");
		query.setParameter("bid", bookId);
		return query.getResultList();
	}

	public List<Activity> listActivityOnUserBooks(int nbElts, Long userId) {
		Defense.notNull(userId, "userId");
		Query query = entityManager.createQuery("select distinct a from " + Activity.class.getName() + " a, " + Book.class.getName()
				+ " b join b.users u where u.id=:uid and a.user.id!=:uid and (a.id in (select id from " + BookActivity.class.getName()
				+ " ba where ba.book.id=b.id) or a.id in (select id from " + CommentActivity.class.getName()
				+ " coa where coa.comment.publication.chapter.book.id=b.id) or a.id in (select id from " + ChapterActivity.class.getName()
				+ " ca where ca.chapter.book.id=b.id)) order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElts);
		return query.getResultList();
	}

	public List<Activity> listUserActivity(int nbElts, Long userId) {
		Defense.notNull(userId, "userId");
		Query query = entityManager.createQuery("select a from " + getEntityType()
				+ " a where a.deletionDate is null and a.user.id=:uid order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElts);
		return query.getResultList();
	}

	public List<Activity> listActivityOnBook(int nbElements, Long userId) {
		Defense.notNull(userId, "userId");
		Query query = entityManager.createQuery("select distinct a from " + Activity.class.getName() + " a, " + Book.class.getName()
				+ " b join b.users u where u.id=:uid and a.user.id=:uid and (a.id in (select id from " + BookActivity.class.getName()
				+ " ba where ba.book.id=b.id) or a.id in (select id from " + CommentActivity.class.getName()
				+ " coa where coa.comment.publication.chapter.book.id=b.id) or a.id in (select id from " + ChapterActivity.class.getName()
				+ " ca where ca.chapter.book.id=b.id)) order by a.creationDate desc");
		query.setParameter("uid", userId);
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listBookCreationActivity(int nbElements) {
		Query query = entityManager.createQuery("from " + BookActivity.class.getName()
				+ " a where a.deletionDate is null and a.type=:type and a.book.deletionDate is null order by a.creationDate desc");
		query.setParameter("type", BookEventType.CREATE);
		query.setMaxResults(nbElements);
		return query.getResultList();
	}

	public List<Activity> listAccountActivity(int nbElts) {
		Query query = entityManager.createQuery("from " + AccountActivity.class.getName()
				+ " a where a.deletionDate is null and a.type=:type and a.user.deletionDate is null order by a.creationDate desc");
		query.setParameter("type", AccountEventType.JOIN);
		query.setMaxResults(nbElts);
		return query.getResultList();
	}

}
