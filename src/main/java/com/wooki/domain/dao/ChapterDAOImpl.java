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
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;

public class ChapterDAOImpl extends WookiGenericDAOImpl<Chapter, Long> implements ChapterDAO
{

    public ChapterDAOImpl(Session session)
    {
        super(session);
    }

    public boolean isAuthor(Long chapterId, String username)
    {
        Defense.notNull(chapterId, "chapterId");
        Query query = this.session
                .createQuery("select count(b) from Book b, Chapter c join b.users as u where c.id=:id and b.id=c.book.id and u.username=:un");
        Long result = (Long) query.setParameter("un", username).setParameter("id", chapterId)
                .uniqueResult();
        return result == 1;
    }

    public List<Object[]> findNext(Long bookId, Long chapterId)
    {
        Defense.notNull(bookId, "bookId");
        Defense.notNull(chapterId, "chapterId");
        Query query = session
                .createQuery(String
                        .format("select item.id, item.title from "
                                + Book.class.getName()
                                + " book, "
                                + Publication.class.getName()
                                + " pub join book.chapters item where book.id=:bid and pub.chapter.id=item.id and pub.deletionDate is null and pub.published = 1 and item.deletionDate is null and index(item) > (select index(item) from item where item.id=:cid) order by index(item) asc"));
        query.setMaxResults(1);
        query.setParameter("bid", bookId);
        query.setParameter("cid", chapterId);

        return query.list();
    }

    public List<Object[]> findPrevious(Long bookId, Long chapterId)
    {
        Defense.notNull(bookId, "bookId");
        Defense.notNull(chapterId, "chapterId");
        Query query = session
                .createQuery(String
                        .format("select item.id, item.title from "
                                + Book.class.getName()
                                + " book, "
                                + Publication.class.getName()
                                + " pub join book.chapters item where book.id=:bid and pub.chapter.id=item.id and pub.deletionDate is null and pub.published = 1 and item.deletionDate is null and index(item) < (select index(item) from item where item.id=:cid) and index(item) > 0 order by index(item) desc"));
        query.setMaxResults(1);
        query.setParameter("bid", bookId);
        query.setParameter("cid", chapterId);

        return query.list();
    }

    public List<Chapter> listChapterInfo(Long bookId)
    {
        Defense.notNull(bookId, "bookId");
        Query query = session
                .createQuery(String
                        .format(
                                "select NEW %s(c.id, c.title, c.lastModified) from %s c where c.book.id=:book and c.deletionDate is null",
                                getEntityType(),
                                getEntityType()));
        query.setParameter("book", bookId);
        return query.list();
    }

    public List<Chapter> listChapters(Long idBook)
    {
        Defense.notNull(idBook, "bookId");
        Query query = this.session.createQuery("from " + getEntityType()
                + " c where c.book.id=:book and c.deletionDate is null");
        List<Chapter> result = (List<Chapter>) query.setParameter("book", idBook).list();
        return result;
    }

    public List<Chapter> listLastModified(Long id, int nbElts)
    {
        Defense.notNull(id, "bookId");
        Query query = this.session
                .createQuery("from "
                        + this.getEntityType()
                        + " c where c.book.id=:booId and c.deletionDate is null order by c.lastModified desc");
        query.setParameter("bookId", id);
        return query.list();
    }

}
