package com.wooki.services.feeds;

import java.util.Map;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.impl.AccountActivityFeed;
import com.wooki.services.feeds.impl.BookActivityFeed;
import com.wooki.services.feeds.impl.ChapterActivityFeed;
import com.wooki.services.feeds.impl.CommentActivityFeed;
import com.wooki.services.feeds.impl.FeedSourceImpl;
import com.wooki.services.feeds.impl.FrontFeedProducer;
import com.wooki.services.feeds.impl.SingleBookFeedProducer;
import com.wooki.services.feeds.impl.UserPublicFeedProducer;

/**
 * Define All services and elements related to Feed creation.
 * 
 * @author ccordenier
 */
public class FeedModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(FeedSource.class, FeedSourceImpl.class);
    }

    /**
     * Strategy for outputting feed content based on activity
     */
    @SuppressWarnings("unchecked")
    public static ActivityFeedWriter buildActivityFeedWriter(
            Map<Class, ActivityFeedWriter> configuration,
            @InjectService("StrategyBuilder") StrategyBuilder builder)
    {

        StrategyRegistry<ActivityFeedWriter> registry = StrategyRegistry.newInstance(
                ActivityFeedWriter.class,
                configuration);

        return builder.build(registry);
    }

    /**
     * Each type of activity is associated to a FeedWriter. This way we can easily render an Feed
     * entry using its corresponding writer.
     */
    @SuppressWarnings("unchecked")
    public void contributeActivityFeedWriter(
            MappedConfiguration<Class, ActivityFeedWriter> configuration,
            @Autobuild AccountActivityFeed accountActivityFeed,
            @Autobuild BookActivityFeed bookActivityFeed,
            @Autobuild ChapterActivityFeed chapterActivityFeed,
            @Autobuild CommentActivityFeed commentActivityFeed)
    {
        configuration.add(AccountActivity.class, accountActivityFeed);
        configuration.add(BookActivity.class, bookActivityFeed);
        configuration.add(ChapterActivity.class, chapterActivityFeed);
        configuration.add(CommentActivity.class, commentActivityFeed);
    }

    /**
     * Contribute sources elements
     * 
     * @param configuration
     */
    public static void contributeFeedSource(
            MappedConfiguration<ActivitySourceType, FeedProducer> configuration,
            @Autobuild SingleBookFeedProducer singleBook, @Autobuild FrontFeedProducer front,
            @Autobuild UserPublicFeedProducer user)
    {
        configuration.add(ActivitySourceType.BOOK, singleBook);
        configuration.add(ActivitySourceType.BOOK_CREATION, front);
        configuration.add(ActivitySourceType.USER_PUBLIC, user);
    }

}
