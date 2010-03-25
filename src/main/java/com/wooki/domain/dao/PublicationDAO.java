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

import com.wooki.domain.model.Publication;

/**
 * Publication will be used to follow book chapter revision.
 * 
 * @author ccordenier
 */
public interface PublicationDAO extends GenericDAO<Publication, Long>
{

    /**
     * Retrieve a revision number for given chapter.
     * 
     * @param chapterId
     * @param revision
     * @return null if the revision does not exist.
     */
    Publication findRevisionById(Long chapterId, Long revision);

    /**
     * Used to retrieve the last revision of a chapter.
     * 
     * @param chapterId
     * @return
     */
    Publication findLastRevision(Long chapterId);

    /**
     * Used to retrieve the last published revision of a chapter.
     * 
     * @param chapterId
     * @return
     */
    Publication findLastPublishedRevision(Long chapterId);

    /**
     * Check if the revision is published.
     * 
     * @param revision
     * @return
     */
    boolean isPublished(Long revision);

}
