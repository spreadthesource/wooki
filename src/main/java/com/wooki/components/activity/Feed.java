//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.components.activity;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Id;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;

import com.wooki.MoreEventResult;
import com.wooki.WookiEventConstants;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.EnumServiceLocator;
import com.wooki.services.activity.ActivityBlockSource;
import com.wooki.services.activity.ActivityDisplayContext;
import com.wooki.services.activity.ActivitySource;
import com.wooki.services.activity.ActivitySourceType;

/**
 * Display activities.
 * 
 * @author ccordenier
 */
public class Feed<T extends Activity>
{

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "Latest Books")
    private String title;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "x460")
    private String size;

    @Property
    @Parameter(defaultPrefix = BindingConstants.BLOCK, allowNull = true)
    private Block titleBlock;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "activity-feed")
    private String clientId;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "BOOK_CREATION")
    private ActivitySourceType type;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "10")
    private int nbElts;

    @Parameter
    private List<Long> context;

    @Inject
    @Id("activities")
    private Block activitiesBlock;

    @Inject
    private ActivityBlockSource activitySource;

    @Inject
    private Environment environment;

    @Inject
    private EnumServiceLocator locator;

    @Property
    private List<Activity> activities;

    @Property
    private Activity current;

    @Property
    private boolean hasMore;

    @Property
    private int loopIdx;

    private int page;

    private ActivitySource source;

    /**
     * Wrapper around current activity entry.
     * 
     * @author ccordenier
     */
    private class FeedActivityDisplayContext implements ActivityDisplayContext
    {

        public Activity getActivity()
        {
            return current;
        }

        public String getStyle()
        {
            return getCurrentStyle();
        }

        public boolean isResourceUnavailable()
        {
            return current.isResourceUnavailable();
        }

    }

    @SetupRender
    public void setupActivitiesList()
    {
        this.source = this.locator.getService(this.type);
        int startIdx = nbElts * page;
        Long [] parameters = context == null ? null : context.toArray(new Long[context.size()]);
        this.activities = this.source.listActivitiesRange(startIdx, this.nbElts, parameters);
        this.hasMore = this.activities.size() == nbElts;
    }

    protected void beginRender()
    {
        this.environment.push(ActivityDisplayContext.class, new FeedActivityDisplayContext());
    }

    protected void cleanupRender()
    {
        this.environment.pop(ActivityDisplayContext.class);
    }

    @OnEvent(value = WookiEventConstants.UPDATE_MORE_CONTEXT, component = "moreFeeds")
    public MoreEventResult moreFeeds(int page)
    {
        this.page = page;
        this.setupActivitiesList();
        if (this.activities.size() == 0) { return null; }
        MoreEventResult result = new MoreEventResult();
        result.setRenderable(this.activitiesBlock);
        result.setHasMore(this.activities.size() == this.nbElts);
        return result;
    }

    /**
     * Select the block to display the current activity.
     * 
     * @return
     */
    public Block getActivityBlock()
    {
        return this.activitySource.getActivityBlock(current);
    }

    public int getMoreContext()
    {
        return (this.page + this.activities.size() / nbElts);
    }

    public String getCurrentStyle()
    {
        return this.loopIdx == 0 && this.page == 0 ? "first" : null;
    }

    public boolean isDisplayBlock()
    {
        return this.titleBlock != null;
    }

    public boolean isResourceAvailable()
    {
        return !this.current.isResourceUnavailable();
    }

}
