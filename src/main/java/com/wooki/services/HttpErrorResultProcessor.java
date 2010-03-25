package com.wooki.services;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.Response;

/**
 * Send error when the event method returns an HttpError instance.
 * 
 * @author ccordenier
 */
public class HttpErrorResultProcessor implements ComponentEventResultProcessor<HttpError>
{

    private final Response response;

    public HttpErrorResultProcessor(Response response)
    {
        super();
        this.response = response;
    }

    public void processResultValue(HttpError value) throws IOException
    {
        if (value != null)
        {
            this.response.sendError(value.getStatus(), value.getMessage());
        }
    }

}