package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * This service is the entry to handle uploaded resources.
 * 
 * @author ccordenier
 */
public interface UploadMediaService
{

    /**
     * This method can be used to stream an uploaded resource on the response output stream.
     * 
     * @param name
     * @throws IOException
     */
    void streamMedia(String name) throws IOException;

    /**
     * Upload a file an return its virtual name that can be used to generate src attributes of the
     * corresponding html element.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    String uploadMedia(UploadedFile file) throws IOException;

}
