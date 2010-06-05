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

import com.wooki.domain.model.Publication;

public class PublicationDAOImpl extends WookiGenericDAOImpl<Publication, Long> implements
        PublicationDAO
{

    public PublicationDAOImpl(Session session)
    {
        super(session);
    }

    public Publication findRevisionById(Long chapterId, Long revision)
    {
        Defense.notNull(chapterId, "chapterId");
        Defense.notNull(revision, "revision");
        Query query = session
                .createQuery("from "
                        + getEntityType()
                        + " p where p.id=:pid and p.chapter.id=:cid and p.deletionDate is null order by p.creationDate desc");
        query.setParameter("cid", chapterId);
        query.setParameter("pid", revision);
        query.setMaxResults(1);
        List<Publication> published = query.list();
        if (published != null && published.size() > 0)
        {
            return published.get(0);
        }
        else
        {
            return null;
        }
    }

    public Publication findLastRevision(Long chapterId)
    {
        Defense.notNull(chapterId, "chapterId");
        Query query = session
                .createQuery("from "
                        + getEntityType()
                        + " p where p.chapter.id=:id and p.deletionDate is null order by p.creationDate desc");
        query.setParameter("id", chapterId);
        query.setMaxResults(10);
        List<Publication> published = query.list();
        if (published != null && published.size() > 0)
        {
            return published.get(0);
        }
        else
        {
            return null;
        }
    }

    public Publication findLastPublishedRevision(Long chapterId)
    {
        Defense.notNull(chapterId, "chapterId");
        Query query = session
                .createQuery("from "
                        + getEntityType()
                        + " p where p.chapter.id=:id and p.deletionDate is null and p.published = 1 order by p.creationDate desc");
        query.setParameter("id", chapterId);
        query.setMaxResults(10);
        List<Publication> published = query.list();
        if (published != null && published.size() > 0)
        {
            return published.get(0);
        }
        else
        {
            return null;
        }
    }

    public boolean isPublished(Long revision)
    {
        Query query = session.createQuery("select p.published from " + getEntityType()
                + " p where p.id=:id and p.deletionDate is null");
        query.setParameter("id", revision);
        List<Boolean> published = query.list();
        if (published != null && published.size() > 0)
        {
            return published.get(0);
        }
        else
        {
            return false;
        }
    }

}
