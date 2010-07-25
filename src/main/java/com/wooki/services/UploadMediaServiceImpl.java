package com.wooki.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.wooki.WookiRequestConstants;
import com.wooki.installer.services.GlobalSettingsTask;

public class UploadMediaServiceImpl implements UploadMediaService
{    
    private final Request request;

    private final Response response;

    private final File uploadDir;

    private String pathPrefix;

    public UploadMediaServiceImpl(Request request, Response response,
            @Inject ApplicationSettings settings)
    {
        super();
        this.request = request;
        this.response = response;
        this.uploadDir = new File(settings.get(GlobalSettingsTask.UPLOAD_DIR));
        this.pathPrefix = RequestConstants.ASSET_PATH_PREFIX
                + WookiRequestConstants.UPLOADED_FOLDER;
    }

    public void streamMedia(String name) throws IOException
    {

        // Read media file
        File media = new File(uploadDir, name);
        FileInputStream fileStream = new FileInputStream(media);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        TapestryInternalUtils.copy(fileStream, stream);

        long lastModified = System.currentTimeMillis();

        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", lastModified + InternalConstants.TEN_YEARS);

        response.setContentLength(stream.size());

        OutputStream output = response.getOutputStream("application/octet-stream");

        stream.writeTo(output);

        output.close();
    }

    public String uploadMedia(UploadedFile file) throws IOException
    {
        String filename = file.getFileName();
        File uploaded = File.createTempFile("upload-", filename
                .substring(filename.lastIndexOf(".")), this.uploadDir);
        file.write(uploaded);

        return this.request.getContextPath() + this.pathPrefix + uploaded.getName();
    }

}
