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

package com.wooki.services.parsers;

import java.util.List;

import com.wooki.domain.model.Comment;

/**
 * Used to handle DOM manipulation for Wooki application.
 * 
 * @author ccordenier
 */
public interface DOMManager
{

    /**
     * Will suround all the content and generate ids.
     * 
     * @param content
     * @param prefix
     *            TODO
     * @return
     */
    public String adaptContent(String content, Long prefix);

    /**
     * Used to re-assign comment ids.
     * 
     * @param comments
     * @param content
     * @param newContent
     */
    void reAssignComment(List<Comment> comments, String content, String newContent);

    /**
     * Generate PDF bookmarks from a document content.
     * 
     * @param content
     * @param startIdx
     *            TODO
     * @param level
     *            TODO
     * @return
     */
    String generatePdfBookmarks(String content, int startIdx, int level);

}
