package com.wooki.base.components;

import org.apache.tapestry5.Link;

public class BookMenuItem {
	private String name;

	private Link link;
	
	private boolean confirm;
	
	private String confirmMsg;

	public BookMenuItem(String name, Link link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}
}
