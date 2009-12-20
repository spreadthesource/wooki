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
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;

/**
 * Display activities.
 * 
 * @author ccordenier
 * 
 */
public class Feed<T extends Activity> {

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "Latest Books")
	private String title;

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

	@Property
	private List<Activity> activities;

	@Property
	private Activity current;

	@SetupRender
	public void setupActivitiesList() {
		if (userId == null) {
			this.activities = this.activityManager
					.listBookCreationActivity(nbElts);
		} else {
			if (ActivityType.USER == type) {
				this.activities = this.activityManager.listActivityOnBook(
						nbElts, userId);
			} else {
				if (ActivityType.CO_AUTHOR == type) {
					this.activities = this.activityManager
							.listActivityOnUserBooks(nbElts, userId);
				} else {
					if (ActivityType.USER_PUBLIC == type) {
						this.activities = this.activityManager
								.listUserActivity(nbElts, userId);
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
			return chapterActivity;
		} else {
			if (current instanceof BookActivity) {
				return bookActivity;
			} else {
				if (current instanceof CommentActivity) {
					return commentActivity;
				}
			}
		}
		return null;
	}

	public boolean isDisplayBlock() {
		return this.titleBlock != null;
	}
}
