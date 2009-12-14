package com.wooki.domain.model;


public enum CommentState {

	OPEN, FIXED, REFUSED, REJECTED;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
	
}
