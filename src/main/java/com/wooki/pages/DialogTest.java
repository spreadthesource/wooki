package com.wooki.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class DialogTest
{
	@Inject
	private Block myBlock;
	
	@Persist
	@Property
	private int count;
	
	@OnEvent(value="action", component="myLink")
	private Block refreshDialog() {
		count++;
		return myBlock;
	}
}
