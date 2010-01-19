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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.model.activity.Activity;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("activityManager")
public class ActivityManagerImpl implements ActivityManager {

	@Autowired
	private ActivityDAO activityDao;

	@Autowired
	private UserDAO userDao;

	public List<Activity> listAll(int nbElts) {
		return this.activityDao.list(nbElts);
	}

	public List<Activity> listActivityOnUserBooks(int nbElts, Long userId) {
		return this.activityDao.listActivityOnUserBooks(nbElts, userId);
	}

	public List<Activity> listUserActivity(int nbElts, Long userId) {
		return this.activityDao.listUserActivity(nbElts, userId);
	}

	public List<Activity> listActivityOnBook(int nbElements, Long userId) {
		return this.activityDao.listActivityOnBook(nbElements, userId);
	}

	public List<Activity> listBookCreationActivity(int nbElements) {
		return this.activityDao.listBookCreationActivity(nbElements);
	}

	public List<Activity> listAccountActivity(int nbElements) {
		return this.activityDao.listAccountActivity(nbElements);
	}

}
