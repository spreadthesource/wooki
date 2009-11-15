package com.wooki.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;

import com.wooki.domain.model.FreshStuff;
import com.wooki.services.ActivityManager;

public class Index {

	@Inject
	private ApplicationContext applicationContext;

	private ActivityManager activityManager;

	@Property
	private List<FreshStuff> freshStuffs;

	@Property
	private FreshStuff current;
	
	@SetupRender
	public void setupFreshStuff() {
		activityManager = (ActivityManager) applicationContext
				.getBean("activityManager");
		freshStuffs = activityManager.listFreshStuff(10);
	}

}
