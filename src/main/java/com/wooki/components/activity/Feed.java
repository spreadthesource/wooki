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
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.ActivityType;
import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;

/**
 * Display activities.
 * 
 * @author ccordenier
 */
public class Feed<T extends Activity> {

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
    private ActivityType type;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "10")
    private int nbElts;

    @Parameter
    private Long userId;

    @Inject
    private ActivityManager activityManager;

    @Inject
    private Block bookActivity;

    @Inject
    private Block chapterActivity;

    @Inject
    private Block commentActivity;

    @Inject
    private Block accountActivity;

    @Property
    private List<Activity> activities;

    @Property
    private Activity current;

    @Property
    private int loopIdx;

    @SetupRender
    public void setupActivitiesList() {
	if (ActivityType.BOOK_CREATION.equals(type)) {
	    this.activities = this.activityManager.listBookCreationActivity(nbElts);
	} else {
	    if (ActivityType.USER.equals(type)) {
		this.activities = this.activityManager.listActivityOnBook(nbElts, userId);
	    } else {
		if (ActivityType.CO_AUTHOR.equals(type)) {
		    this.activities = this.activityManager.listActivityOnUserBooks(nbElts, userId);
		} else {
		    if (ActivityType.USER_PUBLIC.equals(type)) {
			this.activities = this.activityManager.listUserActivity(nbElts, userId);
		    } else {
			if (ActivityType.ACCOUNT.equals(type)) {
			    this.activities = this.activityManager.listAccountActivity(nbElts);
			}
		    }

		}
	    }
	}
    }

    /**
     * Select the block to display the current activity.
     * 
     * @return
     */
    public Block getActivityBlock() {
	if (current instanceof ChapterActivity) {
	    return this.chapterActivity;
	} else {
	    if (current instanceof BookActivity) {
		return this.bookActivity;
	    } else {
		if (current instanceof CommentActivity) {
		    return this.commentActivity;
		} else {
		    if (current instanceof AccountActivity) {
			return this.accountActivity;
		    }
		}
	    }
	}
	return null;
    }

    public String getCurrentStyle() {
	return this.loopIdx == 0 ? "first" : null;
    }

    public boolean isDisplayBlock() {
	return this.titleBlock != null;
    }

    public boolean isResourceAvailable() {
	return !this.current.isResourceUnavailable();
    }

}
