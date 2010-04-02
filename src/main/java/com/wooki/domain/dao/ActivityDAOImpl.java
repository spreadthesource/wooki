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

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.wooki.domain.model.Book;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;

public class ActivityDAOImpl extends WookiGenericDAOImpl<Activity, Long> implements ActivityDAO
{

    public ActivityDAOImpl(Session session)
    {
        super(session);
    }

    public List<Activity> list(int startIdx, int nbElements)
    {
        Query query = session.createQuery("from " + getEntityType()
                + " a where a.deletionDate is null order by a.creationDate desc");
        this.setMaxResults(query, nbElements);
        query.setFirstResult(startIdx);
        return query.list();
    }

    public List<Activity> listAllActivitiesOnComment(Long commentId)
    {
        Defense.notNull(commentId, "commentId");
        Query query = session.createQuery("from " + CommentActivity.class.getName()
                + " ca where ca.comment.id=:cid order by ca.creationDate desc");
        query.setParameter("cid", commentId);
        return query.list();
    }

    public List<Activity> listAllActivitiesOnChapter(Long chapterId)
    {
        Defense.notNull(chapterId, "chapterId");
        Query query = session.createQuery("select a from " + Activity.class.getName()
                + " a where a.id in (select id from " + CommentActivity.class.getName()
                + " coa where coa.comment.publication.chapter.id=:cid) or a.id in (select id from "
                + ChapterActivity.class.getName()
                + " ca where ca.chapter.id=:cid) order by a.creationDate desc");
        query.setParameter("cid", chapterId);
        return query.list();
    }

    public List<Activity> listAllActivitiesOnBook(Long bookId)
    {
        Defense.notNull(bookId, "bookId");
        Query query = session
                .createQuery("select a from "
                        + Activity.class.getName()
                        + " a where a.id in (select id from "
                        + BookActivity.class.getName()
                        + " ba where ba.book.id=:bid) or a.id in (select id from "
                        + CommentActivity.class.getName()
                        + " coa where coa.comment.publication.chapter.book.id=:bid) or a.id in (select id from "
                        + ChapterActivity.class.getName()
                        + " ca where ca.chapter.book.id=:bid) order by a.creationDate desc");
        query.setParameter("bid", bookId);
        return query.list();
    }

    public List<Activity> listActivityOnUserBooks(int startIdx, int nbElts, Long userId)
    {
        Defense.notNull(userId, "userId");
        Query query = session
                .createQuery("select distinct a from "
                        + Activity.class.getName()
                        + " a, "
                        + Book.class.getName()
                        + " b join b.users u where u.id=:uid and a.user.id!=:uid and (a.id in (select id from "
                        + BookActivity.class.getName()
                        + " ba where ba.book.id=b.id) or a.id in (select id from "
                        + CommentActivity.class.getName()
                        + " coa where coa.comment.publication.chapter.book.id=b.id) or a.id in (select id from "
                        + ChapterActivity.class.getName()
                        + " ca where ca.chapter.book.id=b.id)) order by a.creationDate desc");
        query.setParameter("uid", userId);
        this.setMaxResults(query, nbElts);
        query.setFirstResult(startIdx);
        return query.list();
    }

    public List<Activity> listUserActivity(int startIdx, int nbElts, Long userId)
    {
        Defense.notNull(userId, "userId");
        Query query = session
                .createQuery("select a from "
                        + getEntityType()
                        + " a where a.deletionDate is null and a.user.id=:uid order by a.creationDate desc");
        query.setParameter("uid", userId);
        this.setMaxResults(query, nbElts);
        query.setFirstResult(startIdx);
        return query.list();
    }

    public List<Activity> listActivityOnBook(int startIdx, int nbElements, Long userId)
    {
        Defense.notNull(userId, "userId");
        Query query = session
                .createQuery("select distinct a from "
                        + Activity.class.getName()
                        + " a, "
                        + Book.class.getName()
                        + " b join b.users u where u.id=:uid and a.user.id=:uid and (a.id in (select id from "
                        + BookActivity.class.getName()
                        + " ba where ba.book.id=b.id) or a.id in (select id from "
                        + CommentActivity.class.getName()
                        + " coa where coa.comment.publication.chapter.book.id=b.id) or a.id in (select id from "
                        + ChapterActivity.class.getName()
                        + " ca where ca.chapter.book.id=b.id)) order by a.creationDate desc");
        query.setParameter("uid", userId);
        this.setMaxResults(query, nbElements);
        query.setFirstResult(startIdx);
        return query.list();
    }

    public List<Activity> listBookCreationActivity(int startIdx, int nbElements)
    {
        Query query = session
                .createQuery("from "
                        + BookActivity.class.getName()
                        + " a where a.deletionDate is null and a.type=:type and a.book.deletionDate is null order by a.creationDate desc");
        query.setParameter("type", BookEventType.CREATE);
        this.setMaxResults(query, nbElements);
        query.setFirstResult(startIdx);
        return query.list();
    }

    public List<Activity> listAccountActivity(int startIdx, int nbElts)
    {
        Query query = session
                .createQuery("from "
                        + AccountActivity.class.getName()
                        + " a where a.deletionDate is null and a.type=:type and a.user.deletionDate is null order by a.creationDate desc");
        query.setParameter("type", AccountEventType.JOIN);
        this.setMaxResults(query, nbElts);
        query.setFirstResult(startIdx);
        return query.list();
    }

}
