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

import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;

@Repository("commentDao")
public class CommentDAOImpl extends GenericDAOImpl<Comment, Long> implements CommentDAO {

	public boolean isOwner(Long commId, String username) {
		Defense.notNull(commId, "commId");
		Query query = this.entityManager.createQuery("select count(c) from " + this.getEntityType()
				+ " c join c.user as u where c.id=:id and u.username=:un and c.deletionDate is null");
		Long result = (Long) query.setParameter("un", username).setParameter("id", commId).getSingleResult();
		return result > 0;
	}

	public List<Comment> listForPublication(Long publicationId) {
		Defense.notNull(publicationId, "publicationId");
		Query query = this.entityManager
				.createQuery("from " + getEntityType() + " c where c.publication.id=:pubId and c.state!=:st and c.deletionDate is null order by c.creationDate desc");
		query.setParameter("pubId", publicationId);
		query.setParameter("st", CommentState.REJECTED);
		return query.getResultList();
	}

	public List<Comment> listForChapter(Long chapterId) {
		Defense.notNull(chapterId, "chapterId");
		Query query = this.entityManager
				.createQuery("from " + getEntityType() + " c where c.publication.chapter.id=:chapterId and c.state!=:st and c.deletionDate is null order by c.creationDate desc");
		query.setParameter("chapterId", chapterId);
		query.setParameter("st", CommentState.REJECTED);
		return query.getResultList();
	}
	
	public List<Comment> listForPublicationAndDomId(Long publicationId, String domId) {
		Defense.notNull(publicationId, "publicationId");
		Query query = this.entityManager.createQuery("from " + getEntityType()
				+ " c where c.publication.id=:pubId and c.state!=:st and c.domId=:cid and c.deletionDate is null order by c.creationDate desc");
		query.setParameter("pubId", publicationId);
		query.setParameter("st", CommentState.REJECTED);
		query.setParameter("cid", domId);

		return query.getResultList();
	}

	public List<Object[]> listCommentsInfoForPublication(Long publicationId) {
		Defense.notNull(publicationId, "publicationId");
		String queryStr = String.format("select c.domId, count(c.domId) from %s c where c.publication.id=:id and c.state!=:st and c.deletionDate is null group by c.domId",
				Comment.class.getName());
		Query query = this.entityManager.createQuery(queryStr);
		query.setParameter("id", publicationId);
		query.setParameter("st", CommentState.REJECTED);
		return query.getResultList();
	}

}
