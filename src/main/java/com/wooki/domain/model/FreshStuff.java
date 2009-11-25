package com.wooki.domain.model;

/**
 * Used in fresh stuff panel to display last document activity.
 */
public class FreshStuff {

	private String bookTitle;

	private String username;

	private String action;
	
	private String period;
	
	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}
