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

package com.wooki.domain.biz;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.services.security.WookiSecurityContext;

public class CommentManagerImpl implements CommentManager
{
    private final CommentDAO commentDao;

    private final ActivityDAO activityDao;

    private WookiSecurityContext securityCtx;

    public CommentManagerImpl(CommentDAO commentDao, ActivityDAO activityDAO,
            ApplicationContext context)
    {
        this.commentDao = commentDao;
        this.activityDao = activityDAO;

        this.securityCtx = context.getBean(WookiSecurityContext.class);
    }

    public Comment findById(Long commId)
    {
        return commentDao.findById(commId);
    }

    public List<Comment> listForChapter(Long chapterId)
    {
        assert chapterId != null;
        return this.commentDao.listForChapter(chapterId);
    }

    public List<Comment> listOpenForPublication(Long chapterId)
    {
        return commentDao.listForPublication(chapterId);
    }

    public List<Object[]> listCommentInfos(Long publicationId)
    {
        return commentDao.listCommentsInfoForPublication(publicationId);
    }

    public List<Comment> listForPublicationAndDomId(Long publicationId, String domId)
    {
        return commentDao.listForPublicationAndDomId(publicationId, domId);
    }

    public void removeComment(Long commId)
    {

        Comment c = this.commentDao.findById(commId);
        assert c != null;

        if (!securityCtx.isOwner(c)) { throw new AuthorizationException(
                "User is not authorized to remove this comment : " + commId); }
        
        this.commentDao.delete(c);
        CommentActivity ca = new CommentActivity();
        ca.setBook(c.getPublication().getChapter().getBook());
        ca.setCreationDate(Calendar.getInstance().getTime());
        ca.setUser(this.securityCtx.getUser());
        ca.setType(CommentEventType.DELETE);
        ca.setComment(c);
        this.activityDao.create(ca);

        // Flag comment activity as unavailable
        List<Activity> activities = this.activityDao.listAllActivitiesOnComment(c.getId());
        if (activities != null)
        {
            for (Activity ac : activities)
            {
                ac.setResourceUnavailable(true);
                this.activityDao.update(ac);
            }
        }

    }

    public Comment update(Comment comment)
    {
        assert comment != null;
        return commentDao.update(comment);
    }

}
