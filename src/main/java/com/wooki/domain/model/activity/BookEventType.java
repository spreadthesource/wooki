package com.wooki.domain.model.activity;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.MessagesImpl;

import com.wooki.services.utils.LastActivityMessage;

public enum BookEventType {
	
	CREATE, UPDATE, DELETE;
	
	private final static Messages MESSAGES = MessagesImpl
	.forClass(LastActivityMessage.class);
	
	@Override
	public String toString() {
		return MESSAGES.get(super.toString());
	}
	
}
