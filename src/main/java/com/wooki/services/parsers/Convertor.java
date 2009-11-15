package com.wooki.services.parsers;

import java.io.InputStream;

import org.springframework.core.io.Resource;

public interface Convertor {
	public InputStream performTransformation(Resource xmlDocument);
}
