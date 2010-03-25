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

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.services.security.WookiSecurityContext;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("commentManager")
public class CommentManagerImpl implements CommentManager
{

    @Autowired
    private CommentDAO commentDao;

    @Autowired
    private ActivityDAO activityDao;

    @Autowired
    private WookiSecurityContext securityCtx;

    public Comment findById(Long commId)
    {
        return commentDao.findById(commId);
    }

    public List<Comment> listForChapter(Long chapterId)
    {
        Defense.notNull(chapterId, "chapterId");
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeComment(Long commId)
    {
        if (!securityCtx.isAuthorOfComment(commId)) { throw new AuthorizationException(
                "User is not authorized to remove this comment : " + commId); }

        Comment c = this.commentDao.findById(commId);
        Defense.notNull(c, "comment");
        this.commentDao.delete(c);
        CommentActivity ca = new CommentActivity();
        ca.setCreationDate(Calendar.getInstance().getTime());
        ca.setUser(this.securityCtx.getAuthor());
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Comment update(Comment comment)
    {
        Defense.notNull(comment, "comment");
        return commentDao.update(comment);
    }

    public void setCommentDao(CommentDAO commentDao)
    {
        this.commentDao = commentDao;
    }

}
