package com.wooki.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Display fresh information for logged user.
 *
 * @author ccordenier
 *
 */
public class Home extends Index {

	@Inject
	private Block homeBlock;

	@Override
	public Block getUserCtx() {
		return homeBlock;
	}

}
