package com.wooki.pages;

/**
 * Login form.
 * 
 * @author ccordenier
 * 
 */
public class Signin {

	private boolean failed = false;

	public boolean isFailed() {
		return failed;
	}

	void onActivate(String extra) {
		if (extra.equals("failed")) {
			failed = true;
		}
	}
}
