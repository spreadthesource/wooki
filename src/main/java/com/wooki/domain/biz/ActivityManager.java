package com.wooki.domain.biz;

import java.util.List;

import com.wooki.domain.model.Activity;

/**
 * Use to handle activities on wikies element to follow history.
 * 
 * @author ccordenier
 * 
 */
public interface ActivityManager {

	List<Activity> listAll(int nbElts);

}
