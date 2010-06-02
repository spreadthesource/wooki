package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import com.wooki.WookiRequestConstants;

/**
 * This dispatcher identifies the files that has been updated by the user. Doing this we can protect
 * assets from their real name.
 * 
 * @author ccordenier
 */
public class UploadedAssetDispatcher implements Dispatcher
{

    public static final String PATH_PREFIX = RequestConstants.ASSET_PATH_PREFIX
            + WookiRequestConstants.UPLOADED_FOLDER;

    private final UploadMediaService streamer;

    public UploadedAssetDispatcher(UploadMediaService streamer)
    {
        this.streamer = streamer;
    }

    public boolean dispatch(Request request, Response response) throws IOException
    {
        String path = request.getPath();

        if (!path.startsWith(PATH_PREFIX)) return false;

        // PATH_PREFIX includes the slash.

        String fileName = path.substring(PATH_PREFIX.length());

        streamer.streamMedia(fileName);

        return true;
    }

}
