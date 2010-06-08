package com.wooki.services.feeds.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.EnumServiceLocator;
import com.wooki.services.ServicesMessages;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.ActivityFeedWriter;

/**
 * Produces the feed for a single book.
 * 
 * @author ccordenier
 */
public class UserPublicFeedProducer extends AbstractFeedProducer
{

    private final UserManager userManager;

    public UserPublicFeedProducer(@Inject LinkSource linkSource,
            @Inject ActivityFeedWriter<Activity> activityFeed, @Inject ServicesMessages messages,
            @Inject UserManager userManager, EnumServiceLocator locator)
    {
        super(messages, linkSource, activityFeed, locator
                .getService(ActivitySourceType.USER_PUBLIC));
        this.userManager = userManager;
    }

    /**
     * Read book definition and generate corresponding feed.
     */
    public Feed produce(Long... context)
    {

        if (context == null || !(context.length > 0)) { throw new IllegalArgumentException(
                "UserPublicFeedProducer requires the user id as a parameter"); }

        User user = this.userManager.findById(context[0]);

        if (user == null) { throw new IllegalArgumentException(
                "Requested user does not exist requires the user id as a parameter"); }

        String title = this.messages.getMessages().format(
                "recent-user-activity",
                user.getUsername());
        String id = user.getUsername() + "-public";

        List<Link> alternateLinks = new ArrayList<Link>();

        Link linkToSelf = new Link();
        linkToSelf.setHref(lnkSource.createPageRenderLink("index", true, user.getUsername())
                .toAbsoluteURI());
        linkToSelf.setTitle(title);

        alternateLinks.add(linkToSelf);

        return super.fillFeed(id, title, alternateLinks, this.source.listActivitiesForFeed(user
                .getId()));
    }

}
