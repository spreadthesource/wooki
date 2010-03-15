package com.wooki.services.feeds;

import java.util.List;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.ActivityType;
import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.feeds.impl.FeedProducerImpl;

/**
 * Define Feed Sources.
 * 
 * @author ccordenier
 * 
 */
public class FeedModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(FeedProducer.class, FeedProducerImpl.class);
	}

	/**
	 * Contribute sources elements
	 * 
	 * @param configuration
	 */
	public void contributeFeedProducer(MappedConfiguration<ActivityType, FeedSource> configuration, @Inject final ActivityManager manager) {
		configuration.add(ActivityType.BOOK, new FeedSource() {
			public List<Activity> getActivities(Long... ids) {
				return manager.listAllBookActivities(ids[0]);
			}
		});
		configuration.add(ActivityType.BOOK_CREATION, new FeedSource() {
			public List<Activity> getActivities(Long... ids) {
				return manager.listBookCreationActivity(0, -1);
			}
		});
		configuration.add(ActivityType.USER_PUBLIC, new FeedSource() {
			public List<Activity> getActivities(Long... ids) {
				return manager.listUserActivity(0, -1, ids[0]);
			}
		});
	}

}
