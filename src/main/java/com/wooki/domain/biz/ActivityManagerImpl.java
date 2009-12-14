package com.wooki.domain.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.Activity;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("activityManager")
public class ActivityManagerImpl implements ActivityManager {

	@Autowired
	private ActivityDAO activityDao;

	public List<Activity> listAll(int nbElts) {
		return activityDao.list(nbElts);
	}

}
