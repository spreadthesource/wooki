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

@Repository("bookDao")
public class BookDAOImpl extends GenericDAOImpl<Book, Long> implements BookDAO {

	public Book findBookBySlugTitle(String title) {
		Query query = this.entityManager.createQuery("select b from "
				+ getEntityType() + " b where b.slugTitle=:st and b.deletionDate is null");
		List<Book> results = query.setParameter("st", title).getResultList();
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public List<Book> listByOwner(Long id) {
		Defense.notNull(id, "user id");
		Query query = entityManager
				.createQuery("select b from "
						+ getEntityType()
						+ " b where b.owner.id=:uid and b.deletionDate is null");
		query.setParameter("uid", id);
		return (List<Book>) query.getResultList();
	}

	public List<Book> listByCollaborator(Long id) {
		Defense.notNull(id, "user id");
		Query query = entityManager
				.createQuery("select b from "
						+ getEntityType()
						+ " b join b.users as u where b.owner.id!=:uid and u.id=:uid and b.deletionDate is null");
		query.setParameter("uid", id);
		return (List<Book>) query.getResultList();
	}
	
	public List<Book> listByTitle(String title) {
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " b where lower(b.title) like :title and b.deletionDate is null");
		query.setParameter("title", new String("%" + title + "%").toLowerCase());
		return (List<Book>) query.getResultList();
	}

	public boolean isAuthor(Long bookId, String username) {
		Defense.notNull(bookId, "bookId");
		Query query = this.entityManager
				.createQuery("select count(b) from Book b join b.users as u where u.username=:un and b.id=:id");
		Long result = (Long) query.setParameter("un", username).setParameter(
				"id", bookId).getSingleResult();
		return result == 1;
	}
	
	public boolean isOwner(Long bookId, String username) {
		Defense.notNull(bookId, "bookId");
		Query query = this.entityManager
				.createQuery("select count(b) from Book b where b.id=:id and b.owner.username=:un");
		Long result = (Long) query.setParameter("un", username).setParameter(
				"id", bookId).getSingleResult();
		return result == 1;
	}
}
