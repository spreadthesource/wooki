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
		return activityDao.list(nbElts);
	}

	public List<Activity> listActivityOnUserBooks(int nbElts, Long userId) {
		return activityDao.listActivityOnUserBooks(nbElts, userId);
	}

	public List<Activity> listUserActivity(int nbElts, Long userId) {
		return activityDao.listUserActivity(nbElts, userId);
	}

	public List<Activity> listActivityOnBook(int nbElements, Long userId) {
		return activityDao.listActivityOnBook(nbElements, userId);
	}

	public List<Activity> listBookCreationActivity(int nbElements) {
		return activityDao.listBookCreationActivity(nbElements);
	}

}
