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

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import com.wooki.Draft;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.Publication;

/**
 * Interface used to access to chapter related information from the wooki application.
 */
public interface ChapterManager
{

    public static final String LAST = "last";

    /**
     * Add a comment to the list of existing one.
     * 
     * @param chapter
     * @param content
     * @param domId
     */

    @CommitAfter
    Comment addComment(Long publicationId, String content, String domId);

    /**
     * Load a chapter.
     * 
     * @return
     */
    Chapter findById(Long chapterId);

    /**
     * Find next and previous chapter from a chapter id
     * 
     * @param bookId
     * @param chapterId
     * @return
     */
    Object[] findPrevious(Long bookId, Long chapterId);

    /**
     * Find the next published Chapter.
     * 
     * @param bookId
     * @param chapterId
     * @return
     */
    Object[] findNext(Long bookId, Long chapterId);

    /**
     * Retrieve the content for a given chapter. Content is lazy loaded for the sake of performance.
     * TODO:
     * 
     * @param chapter
     * @return
     */
    String getLastContent(Long chapterId);

    /**
     * Retrieve the content for a given chapter. Content is lazy loaded for the sake of performance.
     * 
     * @param revision
     * @param chapter
     * @return
     */
    Publication getRevision(Long chapterId, String revision);

    /**
     * Check if the revision is published.
     * 
     * @param revision
     * @return
     */
    boolean isPublished(Long revision);

    /**
     * Publish chapter content
     * 
     * @param chapterId
     */
    @CommitAfter
    void publishChapter(Long chapter);

    /**
     * Find the last published chapter content.
     * 
     * @param chapterId
     * @return
     */
    String getLastPublishedContent(Long chapterId);

    /**
     * Get the last publication for a given chapter. TODO:
     * 
     * @param chapterId
     * @return
     */
    Publication getLastPublishedPublication(Long chapterId);

    /**
     * Simply update the entity.
     * 
     * @param chapter
     * @return
     */
    @CommitAfter
    Chapter update(Chapter chapter);

    /**
     * When a chapter content is updated then all its related comments must be re-organized for the
     * sake of consistency.
     * 
     * @param chapter
     */
    @CommitAfter
    void updateContent(Long chapterId, Draft draft);

    /**
     * Update and publish a chapter content. When a chapter content is updated then all its related
     * comments must be re-organized for the sake of consistency.
     * 
     * @param chapter
     */
    @CommitAfter
    void updateAndPublishContent(Long chapterId, Draft draft);

    /**
     * Remove chapter from book.
     * 
     * @param book
     * @param chapterId
     */
    @CommitAfter
    void remove(Long chapterId);

    /**
     * List the chapters for a given book.
     * 
     * @param bookId
     * @return
     */
    List<Chapter> listChapters(Long bookId);

    /**
     * List the chapters for a given book, the book abstract chapter is exclude from this list.
     * 
     * @param bookId
     * @return
     */
    List<Chapter> listChaptersInfo(Long bookId);

}
