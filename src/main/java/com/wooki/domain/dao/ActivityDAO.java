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

import com.wooki.domain.model.activity.Activity;
import com.wooki.services.db.query.QueryFilter;

/**
 * Manipulate activity elements
 * 
 * @author ccordenier
 */
public interface ActivityDAO extends GenericDAO<Activity, Long>
{

    public List<Activity> list(QueryFilter... filters);

    public List<Activity> listAllActivitiesOnComment(Long commentId, QueryFilter... filters);

    public List<Activity> listAllActivitiesOnChapter(Long chapterId, QueryFilter... filters);

    public List<Activity> listAllActivitiesOnBook(Long bookId, QueryFilter... filters);

    public List<Activity> listCoauthorBookActivity(Long userId, QueryFilter... filters);

    public List<Activity> listUserActivity(Long userId, QueryFilter... filters);

    public List<Activity> listActivityOnBook(Long bookId, QueryFilter... filters);

    public List<Activity> listBookCreationActivity(QueryFilter... filters);

    public List<Activity> listAccountActivity(QueryFilter... filters);

    public List<Activity> listUserPublicActivity(Long userId, QueryFilter... filters);

    
}
