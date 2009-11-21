package com.wooki.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.model.Activity;
import com.wooki.domain.model.FreshStuff;
import com.wooki.services.utils.LastActivityMessage;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service("activityManager")
public class ActivityManagerImpl implements ActivityManager {

	private final static Messages MESSAGES = MessagesImpl
			.forClass(LastActivityMessage.class);

	@Autowired
	private ActivityDAO activityDao;

	public List<Activity> listAll(int nbElts) {
		return activityDao.list(nbElts);
	}

	public List<FreshStuff> listFreshStuff(int nbElts) {
		List<FreshStuff> freshStuffs = new ArrayList<FreshStuff>();
		List<Activity> activities = activityDao.list(nbElts);
		for (Activity activity : activities) {
			FreshStuff stuff = new FreshStuff();
			stuff.setBookTitle(activity.getBookTitle());
			stuff.setUsername(activity.getUsername());
			stuff.setPeriod(LastActivityMessage.getActivityPeriod(activity.getEventDate().getTime()));
			switch (activity.getType()) {
			case CREATE:
				stuff.setAction(MESSAGES.get("element-created"));
				break;
			case UPDATE:
				stuff.setAction(MESSAGES.get("element-updated"));
				break;
			case DELETE:
				stuff.setAction(MESSAGES.get("element-deleted"));
				break;

			default:
				break;
			}
			freshStuffs.add(stuff);
		}
		return freshStuffs;
	}

}
