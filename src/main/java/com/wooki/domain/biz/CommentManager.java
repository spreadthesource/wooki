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

import com.wooki.domain.model.Comment;

/**
 * Use to manage comments in the wooki application.
 * 
 * @author ccordenier
 */
public interface CommentManager
{

    /**
     * Update the comment
     * 
     * @param comment
     * @return
     */
    Comment update(Comment comment);

    /**
     * Remove a comment.
     * 
     * @param commId
     */
    void removeComment(Long commId);

    /**
     * Find a comment by id.
     * 
     * @param commId
     * @return
     */
    Comment findById(Long commId);

    /**
     * List all the comment for a book including comments associated to chapters.
     * 
     * @param bookId
     * @return
     */
    List<Comment> listForChapter(Long chapterId);

    /**
     * List all the comments associated the given chapter.
     * 
     * @return
     */
    List<Comment> listOpenForPublication(Long chapterId);

    /**
     * @param chapterId
     * @return
     */
    List<Object[]> listCommentInfos(Long chapterId);

    /**
     * @param chapterId
     * @return
     */
    List<Comment> listForPublicationAndDomId(Long chapterId, String domId);

}
