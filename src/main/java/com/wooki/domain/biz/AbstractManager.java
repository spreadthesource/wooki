package com.wooki.domain.biz;

import java.io.UnsupportedEncodingException;

public abstract class AbstractManager {
	protected static final <T> void protectionNotNull(T param) {
		if (param == null) {
			// wow , i'm sure this is totally ugly to cast a new object to the
			// type T, but is there any way to do it better?
			throw new IllegalArgumentException("Object of type: '" + ((T) new Object()).getClass().getName() + "' must not be null.");
		}
	}

	protected static final String toStringWithCharset(byte[] content, String charset) {
		try {
			return new String(content, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
