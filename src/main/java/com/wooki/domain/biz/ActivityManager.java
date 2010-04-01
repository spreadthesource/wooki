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

import com.wooki.domain.model.activity.Activity;

/**
 * Use to handle activities on wikies element to follow history.
 */
public interface ActivityManager
{

    List<Activity> listAll(int startIdx, int nbElts);

    List<Activity> listAllBookActivities(Long bookId);

    List<Activity> listUserActivity(int startIdx, int nbElts, Long userId);

    List<Activity> listActivityOnUserBooks(int startIdx, int nbElts, Long userId);

    /**
     * List the book creation activity.
     * 
     * @param startIdx
     * @return
     */
    List<Activity> listBookCreationActivity(int startIdx, int nbElements);

    /**
     * List the activity of a user on its own books.
     * 
     * @param startIdx
     * @param userId
     * @param nbElementsn
     * @return
     */
    List<Activity> listActivityOnBook(int startIdx, int nbElements, Long userId);

    /**
     * List the account activity on wooki.
     * 
     * @param startIdx
     * @param nbElements
     * @param userId
     * @return
     */
    List<Activity> listAccountActivity(int startIdx, int nbElements);
}
