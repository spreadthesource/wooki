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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.wooki.domain.model.activity.AbstractBookActivity;
import com.wooki.domain.model.activity.AbstractChapterActivity;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.BookEventType;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.services.db.QueryFilter;

public class ActivityDAOImpl extends WookiGenericDAOImpl<Activity, Long> implements ActivityDAO
{

    public ActivityDAOImpl(Session session)
    {
        super(session);
    }

    @Override
    protected void applyFilters(Criteria crit, QueryFilter... filters)
    {
        super.applyFilters(crit, filters);
        crit.addOrder(Order.desc("creationDate"));
    }

    public List<Activity> list(QueryFilter... filters)
    {
        Criteria crit = session.createCriteria(Activity.class);
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listAllActivitiesOnComment(Long commentId, QueryFilter... filters)
    {
        assert commentId != null;
        Criteria crit = session.createCriteria(CommentActivity.class);
        crit.add(Restrictions.eq("comment.id", commentId));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listAllActivitiesOnChapter(Long chapterId, QueryFilter... filters)
    {
        assert chapterId != null;
        Criteria crit = session.createCriteria(AbstractChapterActivity.class);
        crit.add(Restrictions.eq("chapter.id", chapterId));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listAllActivitiesOnBook(Long bookId, QueryFilter... filters)
    {
        assert bookId != null;
        Criteria crit = session.createCriteria(AbstractBookActivity.class);
        crit.add(Restrictions.eq("book.id", bookId));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listCoauthorBookActivity(Long userId, QueryFilter... filters)
    {
        assert userId != null;

        Criteria crit = session.createCriteria(AbstractBookActivity.class);
        crit.add(Restrictions.ne("user.id", userId)).createCriteria("book").createAlias(
                "users",
                "u").add(Restrictions.eq("u.id", userId));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listUserActivity(Long userId, QueryFilter... filters)
    {
        assert userId != null;

        Criteria crit = session.createCriteria(AbstractBookActivity.class, "a");
        crit.add(Restrictions.eq("a.user.id", userId));
        applyFilters(crit, filters);

        return crit.list();
    }

    public List<Activity> listUserPublicActivity(Long userId, QueryFilter... filters)
    {
        assert userId != null;

        Criteria crit = session.createCriteria(AbstractBookActivity.class, "a");
        crit.add(Restrictions.eq("a.user.id", userId));
        crit.add(Restrictions.eq("a.resourceUnavailable", false));
        applyFilters(crit, filters);

        return crit.list();
    }

    public List<Activity> listActivityOnBook(Long userId, QueryFilter... filters)
    {
        assert userId != null;

        Criteria crit = session.createCriteria(AbstractBookActivity.class);
        crit.add(Restrictions.eq("user.id", userId)).createCriteria("book").createAlias(
                "users",
                "u").add(Restrictions.eq("u.id", userId));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listBookCreationActivity(QueryFilter... filters)
    {
        Criteria crit = session.createCriteria(BookActivity.class);
        crit.add(Restrictions.eq("type", BookEventType.CREATE));
        applyFilters(crit, filters);
        return crit.list();
    }

    public List<Activity> listAccountActivity(QueryFilter... filters)
    {
        Criteria crit = session.createCriteria(AccountActivity.class);
        crit.add(Restrictions.eq("type", AccountEventType.JOIN));
        applyFilters(crit, filters);
        return crit.list();
    }

}
