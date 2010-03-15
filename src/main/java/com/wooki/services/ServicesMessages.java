package com.wooki.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.UpdateListener;

public interface ServicesMessages extends UpdateListener {

	Messages getMessages();
	
}
