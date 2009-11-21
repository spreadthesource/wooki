package com.wooki.services;

import java.util.List;

import com.wooki.domain.model.Activity;
import com.wooki.domain.model.FreshStuff;

/**
 * Use to handle activities on wikies element to follow history.
 * 
 * @author ccordenier
 * 
 */
public interface ActivityManager {

	List<Activity> listAll(int nbElts);

	List<FreshStuff> listFreshStuff(int nbElts);

}
