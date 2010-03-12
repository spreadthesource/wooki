package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.upload.services.UploadedFile;

public interface UploadMediaService {

	void streamMedia(String name) throws IOException;

	String uploadMedia(UploadedFile file) throws IOException;

}
