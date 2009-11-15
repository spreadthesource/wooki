package com.wooki.services;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.model.Activity;
import com.wooki.domain.model.FreshStuff;

/**
 * Use to handle activities on wikies element to follow history.
 * 
 * @author ccordenier
 * 
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface ActivityManager {

	List<Activity> listAll(int nbElts);

	List<FreshStuff> listFreshStuff(int nbElts);
	
}
